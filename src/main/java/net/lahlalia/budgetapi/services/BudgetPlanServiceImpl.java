package net.lahlalia.budgetapi.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lahlalia.budgetapi.dtos.BudgetPlanDto;
import net.lahlalia.budgetapi.entities.BudgetPlan;
import net.lahlalia.budgetapi.entities.User;
import net.lahlalia.budgetapi.mappers.BudgetPlanMapper;
import net.lahlalia.budgetapi.repositories.BudgetPlanRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BudgetPlanServiceImpl implements BudgetPlanService {

    private final BudgetPlanMapper budgetPlanMapper;
    private final BudgetPlanRepository budgetPlanRepository;


    @Override
    public Integer save(BudgetPlanDto request, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        BudgetPlan budgetPlan = budgetPlanMapper.toEntity(request);
        budgetPlan.setUser(user);
        return budgetPlanRepository.save(budgetPlan).getId();
    }
}
