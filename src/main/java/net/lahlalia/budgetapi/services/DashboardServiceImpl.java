package net.lahlalia.budgetapi.services;

import lombok.RequiredArgsConstructor;
import net.lahlalia.budgetapi.dtos.DashboardResponseDto;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService{

    private final TransactionService transactionService;
    private final BudgetPlanService budgetPlanService;


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
}
