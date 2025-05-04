package net.lahlalia.budgetapi.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.lahlalia.budgetapi.dtos.BudgetPlanDto;
import net.lahlalia.budgetapi.services.BudgetPlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/budget")
@RequiredArgsConstructor
public class BudgetPlanController {
    private final BudgetPlanService budgetPlanService;

    @PostMapping("/")
    public ResponseEntity<Integer> saveBudgetPlan(
            @Valid @RequestBody BudgetPlanDto request,
            Authentication connectedUser
            ){
        return ResponseEntity.ok(budgetPlanService.save(request, connectedUser));
    }

}
