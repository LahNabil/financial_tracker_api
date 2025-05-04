package net.lahlalia.budgetapi.services;

import net.lahlalia.budgetapi.dtos.BudgetPlanDto;
import net.lahlalia.budgetapi.dtos.PageResponse;
import org.springframework.security.core.Authentication;

public interface BudgetPlanService {
    Integer save(BudgetPlanDto request, Authentication connectedUser);
    BudgetPlanDto findById(Integer id, Authentication connectedUser);
    PageResponse<BudgetPlanDto> findAllBudgets(int page, int size, Authentication connectedUser);


    BudgetPlanDto updateBudgetPlan(Integer id, BudgetPlanDto request, Authentication connectedUser);
}
