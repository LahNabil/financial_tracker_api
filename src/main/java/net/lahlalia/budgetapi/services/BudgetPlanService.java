package net.lahlalia.budgetapi.services;

import net.lahlalia.budgetapi.dtos.BudgetChartDataDto;
import net.lahlalia.budgetapi.dtos.BudgetPlanDto;
import net.lahlalia.budgetapi.dtos.PageResponse;
import org.springframework.security.core.Authentication;

import java.util.UUID;

public interface BudgetPlanService {
    UUID save(BudgetPlanDto request, Authentication connectedUser);
    BudgetPlanDto findById(UUID id, Authentication connectedUser);
    PageResponse<BudgetPlanDto> findAllBudgets(int page, int size, Authentication connectedUser);


    BudgetPlanDto updateBudgetPlan(UUID id, BudgetPlanDto request, Authentication connectedUser);

    void deleteBudgetPlan(UUID id, Authentication connectedUser);
    BudgetChartDataDto getCurrentMonthBudgetWithTransactions(Authentication connectedUser);

    UUID findByMonthAndYear(int currentMonth, int currentYear, Authentication connectedUser);
}
