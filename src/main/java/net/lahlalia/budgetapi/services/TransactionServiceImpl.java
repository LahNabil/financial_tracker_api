package net.lahlalia.budgetapi.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lahlalia.budgetapi.dtos.CategorySummaryDto;
import net.lahlalia.budgetapi.dtos.PageResponse;
import net.lahlalia.budgetapi.dtos.TransactionDto;
import net.lahlalia.budgetapi.dtos.UpdateTransactionDto;
import net.lahlalia.budgetapi.entities.BudgetPlan;
import net.lahlalia.budgetapi.entities.Transaction;
import net.lahlalia.budgetapi.entities.User;
import net.lahlalia.budgetapi.enums.TransactionStatus;
import net.lahlalia.budgetapi.enums.TransactionType;
import net.lahlalia.budgetapi.mappers.TransactionMapper;
import net.lahlalia.budgetapi.repositories.BudgetPlanRepository;
import net.lahlalia.budgetapi.repositories.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService{
    private final BudgetPlanRepository budgetPlanRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public UUID save(TransactionDto request, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();

        // Validate if the BudgetPlan exists and if the user is authorized
        BudgetPlan budgetPlan = budgetPlanRepository.findById(request.getBudgetPlanId())
                .orElseThrow(() -> new EntityNotFoundException("No BudgetPlan found"));

        if (!budgetPlan.getUser().getId().equals(user.getId())) {
            throw new EntityNotFoundException("You are not authorized to add a transaction to this BudgetPlan");
        }

        // Validate that the transaction date matches the budget's month and year
        LocalDate transactionDate = request.getDate();
        YearMonth budgetMonth = YearMonth.of(budgetPlan.getYear(), budgetPlan.getMonth());
        YearMonth transactionMonth = YearMonth.from(transactionDate);

        if (!budgetMonth.equals(transactionMonth)) {
            throw new IllegalArgumentException("Transaction date must be in the same month and year as the BudgetPlan");
        }

        // Create and save the transaction
        Transaction transaction = transactionMapper.toEntity(request);
        transaction.setId(null);  // Ensure no ID is set for the new transaction
        transaction.setBudgetPlan(budgetPlan);  // Set the associated budget plan

        return transactionRepository.save(transaction).getId();
    }


    @Override
    public PageResponse<TransactionDto> findAllTransactionsByBudget(UUID budgetId, int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        BudgetPlan budgetPlan = budgetPlanRepository.findById(budgetId)
                .orElseThrow(() -> new EntityNotFoundException("No BudgetPlan found with ID: " + budgetId));

        if (!budgetPlan.getUser().getId().equals(user.getId())) {
            throw new EntityNotFoundException("You are not authorized to view transactions of this BudgetPlan");
        }
        Pageable pageable = PageRequest.of(page,size);
        Page<Transaction> transactionPage = transactionRepository.findAllByBudgetId(budgetId,pageable);
        List<TransactionDto> transactionDtoList = transactionPage.stream()
                .map(transactionMapper::toDto)
                .toList();
        return new PageResponse<>(
                transactionDtoList,
                transactionPage.getNumber(),
                transactionPage.getSize(),
                transactionPage.getTotalElements(),
                transactionPage.getTotalPages(),
                transactionPage.isFirst(),
                transactionPage.isLast()

        );
    }

    @Override
    public TransactionDto updateTransaction(UUID id, UpdateTransactionDto request, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Transaction existingTransaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found with ID: " + id));
        BudgetPlan budgetPlan = existingTransaction.getBudgetPlan();
        if (!budgetPlan.getUser().getId().equals(user.getId())) {
            throw new EntityNotFoundException("You are not authorized to edit this transaction");
        }
        // 3. Vérifier que la nouvelle date correspond au mois/année du budget
        LocalDate newDate = request.getDate();
        YearMonth budgetMonth = YearMonth.of(budgetPlan.getYear(), budgetPlan.getMonth());
        YearMonth newTransactionMonth = YearMonth.from(newDate);
        if (!budgetMonth.equals(newTransactionMonth)) {
            throw new IllegalArgumentException("Transaction date must match the month and year of the BudgetPlan");
        }
        existingTransaction.setTitle(request.getTitle());
        existingTransaction.setAmount(request.getAmount());
        existingTransaction.setDate(request.getDate());
        existingTransaction.setStatus(request.getStatus());
        existingTransaction.setCategory(request.getCategory());
        existingTransaction.setType(request.getType());
        existingTransaction.setDescription(request.getDescription());

        Transaction updatedTransaction = transactionRepository.save(existingTransaction);
        return transactionMapper.toDto(updatedTransaction);
    }

    @Override
    public void deleteTransaction(UUID id, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No transaction found with ID: " + id));

        if (!transaction.getBudgetPlan().getUser().getId().equals(user.getId())) {
            throw new EntityNotFoundException("You are not authorized to delete this transaction");
        }
        transactionRepository.delete(transaction);
    }
    @Override
    public TransactionDto getTransactionById(UUID id, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());

        // Find the transaction
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No transaction found with ID: " + id));

        // Check authorization
        if (!transaction.getBudgetPlan().getUser().getId().equals(user.getId())) {
            throw new EntityNotFoundException("You are not authorized to view this transaction");
        }

        // Map to DTO and return
        return transactionMapper.toDto(transaction);
    }

    @Override
    public BigDecimal getTotalExpenseByBudget(UUID budgetId, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();

        // Check if the budget belongs to the user
        BudgetPlan budgetPlan = budgetPlanRepository.findById(budgetId)
                .orElseThrow(() -> new EntityNotFoundException("No budget found with ID: " + budgetId));

        if (!budgetPlan.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not authorized to access this budget.");
        }

        // Fetch the total expense for the real transactions only
        return transactionRepository.sumAmountByBudgetIdAndTypeAndStatus(budgetId, TransactionType.EXPENSE, TransactionStatus.REAL)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal getTotalIncomeByBudget(UUID budgetId, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();

        // Check if the budget belongs to the user
        BudgetPlan budgetPlan = budgetPlanRepository.findById(budgetId)
                .orElseThrow(() -> new EntityNotFoundException("No budget found with ID: " + budgetId));

        if (!budgetPlan.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not authorized to access this budget.");
        }

        // Fetch the total income for the real transactions only
        return transactionRepository.sumAmountByBudgetIdAndTypeAndStatus(budgetId, TransactionType.INCOME, TransactionStatus.REAL)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal getRemainingBudget(UUID budgetId, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();

        // Check if the budget belongs to the user
        BudgetPlan budgetPlan = budgetPlanRepository.findById(budgetId)
                .orElseThrow(() -> new EntityNotFoundException("No budget found with ID: " + budgetId));

        if (!budgetPlan.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not authorized to access this budget.");
        }

        // Fetch the initial income of the budget plan
        BigDecimal initialIncome = budgetPlan.getInitialIncome();

        // Fetch the total real income (as you already have a method for this)
        BigDecimal totalIncome = transactionRepository.sumAmountByBudgetIdAndTypeAndStatus(budgetId, TransactionType.INCOME, TransactionStatus.REAL)
                .orElse(BigDecimal.ZERO);

        // Fetch the total real expense (similarly as for income)
        BigDecimal totalExpense = transactionRepository.sumAmountByBudgetIdAndTypeAndStatus(budgetId, TransactionType.EXPENSE, TransactionStatus.REAL)
                .orElse(BigDecimal.ZERO);

        // Calculate the remaining budget
        return initialIncome.subtract(totalExpense).add(totalIncome);
    }

    @Override
    public List<CategorySummaryDto> getExpensesByCategory(UUID budgetId, Authentication connectedUser) {

        return transactionRepository.getExpensesByCategory(budgetId);
    }

    @Override
    public List<TransactionDto> findTransactionsByStatus(UUID budgetId, TransactionStatus status, Authentication connectedUser) {
        // (Optional) check if user has access to this budget
        List<Transaction> transactions;

        if (status == TransactionStatus.REAL) {
            transactions = transactionRepository.findRealTransactionsByBudget(budgetId);
        } else if (status == TransactionStatus.EXPECTED) {
            transactions = transactionRepository.findExpectedTransactionsByBudget(budgetId);
        } else {
            transactions = List.of(); // empty list for unknown status
        }

        return transactions.stream()
                .map(transactionMapper::toDto)
                .toList();
    }



}
