package net.lahlalia.budgetapi.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lahlalia.budgetapi.dtos.TransactionDto;
import net.lahlalia.budgetapi.entities.BudgetPlan;
import net.lahlalia.budgetapi.entities.Transaction;
import net.lahlalia.budgetapi.entities.User;
import net.lahlalia.budgetapi.mappers.TransactionMapper;
import net.lahlalia.budgetapi.repositories.BudgetPlanRepository;
import net.lahlalia.budgetapi.repositories.TransactionRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService{
    private final BudgetPlanRepository budgetPlanRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public Integer save(TransactionDto request, Authentication connectedUser) {
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
        transaction.setBudgetPlan(budgetPlan);


        return transactionRepository.save(transaction).getId();
    }
}
