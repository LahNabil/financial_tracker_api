package net.lahlalia.budgetapi.services;

import net.lahlalia.budgetapi.dtos.BudgetPlanDto;
import org.springframework.security.core.Authentication;

public interface BudgetPlanService {
    Integer save(BudgetPlanDto request, Authentication connectedUser);

}
