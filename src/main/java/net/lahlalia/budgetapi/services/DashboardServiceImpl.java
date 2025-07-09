package net.lahlalia.budgetapi.services;

import lombok.RequiredArgsConstructor;
import net.lahlalia.budgetapi.dtos.DashboardResponseDto;
import net.lahlalia.budgetapi.dtos.PageResponse;
import net.lahlalia.budgetapi.dtos.TransactionComparisonDto;
import net.lahlalia.budgetapi.dtos.TransactionDto;
import net.lahlalia.budgetapi.enums.TransactionStatus;
import net.lahlalia.budgetapi.enums.TransactionType;
import net.lahlalia.budgetapi.repositories.TransactionRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService{

    private final TransactionService transactionService;
    private final BudgetPlanService budgetPlanService;
    private final TransactionRepository transactionRepository;


    @Override
    public DashboardResponseDto getDashboardData(UUID budgetId, Authentication connectedUser) {
        if(budgetId == null){
            int currentMonth = LocalDate.now().getMonthValue();
            int currentYear = LocalDate.now().getYear();
            budgetId = budgetPlanService.findByMonthAndYear(currentMonth, currentYear, connectedUser);
        }
        var totalIncome = transactionService.getTotalIncomeByBudget(budgetId, connectedUser);
        var totalExpenses = transactionService.getTotalExpenseByBudget(budgetId, connectedUser);
        var categoryBreakdown = transactionService.getExpensesByCategory(budgetId, connectedUser);

        return DashboardResponseDto.builder()
                .totalIncome(totalIncome)
                .totalExpenses(totalExpenses)
                .expensesByCategory(categoryBreakdown)
                .build();
    }

    @Override
    public TransactionComparisonDto getTransactionComparison(UUID budgetId, Authentication connectedUser) {
        if(budgetId == null){
            int currentMonth = LocalDate.now().getMonthValue();
            int currentYear = LocalDate.now().getYear();
            budgetId = budgetPlanService.findByMonthAndYear(currentMonth, currentYear, connectedUser);
        }

        List<TransactionDto> realTransactions = transactionService.findTransactionsByStatus(budgetId, TransactionStatus.REAL, connectedUser);
        List<TransactionDto> expectedTransactions = transactionService.findTransactionsByStatus(budgetId, TransactionStatus.EXPECTED, connectedUser);


        return new TransactionComparisonDto(realTransactions, expectedTransactions);
    }
}
