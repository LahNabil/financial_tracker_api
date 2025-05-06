package net.lahlalia.budgetapi.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lahlalia.budgetapi.dtos.PageResponse;
import net.lahlalia.budgetapi.dtos.TransactionDto;
import net.lahlalia.budgetapi.dtos.UpdateTransactionDto;
import net.lahlalia.budgetapi.entities.BudgetPlan;
import net.lahlalia.budgetapi.entities.Transaction;
import net.lahlalia.budgetapi.entities.User;
import net.lahlalia.budgetapi.mappers.TransactionMapper;
import net.lahlalia.budgetapi.repositories.BudgetPlanRepository;
import net.lahlalia.budgetapi.repositories.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

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
        User user = ((User) connectedUser.getPrincipal());
        BudgetPlan budgetPlan = budgetPlanRepository.findById(request.getBudgetPlanId())
                .orElseThrow(()-> new EntityNotFoundException("No BudgetPlan found"));
        if (!budgetPlan.getUser().getId().equals(user.getId())) {
            throw new EntityNotFoundException("You are not authorized to add a transaction to this BudgetPlan");
        }
        LocalDate transactionDate = request.getDate();
        YearMonth budgetMonth = YearMonth.of(budgetPlan.getYear(), budgetPlan.getMonth());
        YearMonth transactionMonth = YearMonth.from(transactionDate);
        if (!budgetMonth.equals(transactionMonth)) {
            throw new IllegalArgumentException("Transaction date must be in the same month and year as the BudgetPlan");
        }
        Transaction transaction = transactionMapper.toEntity(request);
        transaction.setId(null);
        transaction.setBudgetPlan(budgetPlan);


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
}
