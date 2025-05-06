package net.lahlalia.budgetapi.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lahlalia.budgetapi.dtos.BudgetPlanDto;
import net.lahlalia.budgetapi.dtos.PageResponse;
import net.lahlalia.budgetapi.entities.BudgetPlan;
import net.lahlalia.budgetapi.entities.User;
import net.lahlalia.budgetapi.mappers.BudgetPlanMapper;
import net.lahlalia.budgetapi.repositories.BudgetPlanRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class BudgetPlanServiceImpl implements BudgetPlanService {

    private final BudgetPlanMapper budgetPlanMapper;
    private final BudgetPlanRepository budgetPlanRepository;


    @Override
    public UUID save(BudgetPlanDto request, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        BudgetPlan budgetPlan = budgetPlanMapper.toEntity(request);
        budgetPlan.setUser(user);
        return budgetPlanRepository.save(budgetPlan).getId();
    }

    @Override
    public BudgetPlanDto findById(UUID id, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        BudgetPlan budgetPlan = budgetPlanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No budget Plan found with this ID"));
        if(!budgetPlan.getUser().getId().equals(user.getId())){
            throw new AccessDeniedException("You are not authorized to access this budget plan.");
        }
        return budgetPlanMapper.toDto(budgetPlan);

//        return budgetPlanRepository.findById(id)
//                .map(budgetPlanMapper::toDto)
//                .orElseThrow(()-> new EntityNotFoundException("No budget Plan found with this ID"));
//
    }

    @Override
    public PageResponse<BudgetPlanDto> findAllBudgets(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BudgetPlan> budgetPlans = budgetPlanRepository.findAllByUserId(user.getId(), pageable);
        List<BudgetPlanDto> budgetPlanDtoList = budgetPlans.stream()
                .map(budgetPlanMapper::toDto)
                .toList();
//        List<BudgetPlanDto> content = budgetPlans
//                .getContent()
//                .stream()
//                .map(budgetPlanMapper::toDto)
//                .collect(Collectors.toList());
        return new PageResponse<>(
                budgetPlanDtoList,
                budgetPlans.getNumber(),
                budgetPlans.getSize(),
                budgetPlans.getTotalElements(),
                budgetPlans.getTotalPages(),
                budgetPlans.isFirst(),
                budgetPlans.isLast()

        );
    }

    @Override
    public BudgetPlanDto updateBudgetPlan(UUID id, BudgetPlanDto request, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        BudgetPlan budgetPlan = budgetPlanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No Budget Plan found with this ID"));
        if (!budgetPlan.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not authorized to update this budget plan");
        }
        budgetPlan.setMonth(request.getMonth());
        budgetPlan.setInitialIncome(request.getInitialIncome());

        BudgetPlan updated = budgetPlanRepository.save(budgetPlan);
        return budgetPlanMapper.toDto(updated);
    }

    @Override
    public void deleteBudgetPlan(UUID id, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        BudgetPlan budgetPlan = budgetPlanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No Budget Plan found with this ID"));

        if (!budgetPlan.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not authorized to delete this budget plan");
        }
        budgetPlanRepository.delete(budgetPlan);

    }
}
