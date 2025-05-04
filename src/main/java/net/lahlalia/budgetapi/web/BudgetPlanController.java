package net.lahlalia.budgetapi.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.lahlalia.budgetapi.dtos.BudgetPlanDto;
import net.lahlalia.budgetapi.dtos.PageResponse;
import net.lahlalia.budgetapi.services.BudgetPlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/{id}")
    public ResponseEntity<BudgetPlanDto> findBudgetPlanById(
            @PathVariable("id") Integer id,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(budgetPlanService.findById(id, connectedUser));
    }

    @GetMapping("/")
    public ResponseEntity<PageResponse<BudgetPlanDto>> findAllBudgets(
            @RequestParam(name = "page", defaultValue = "0", required = false)int page,
            @RequestParam(name = "size", defaultValue = "10", required = false)int size,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(budgetPlanService.findAllBudgets(page, size, connectedUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetPlanDto> updateBudgetPlan(
            @PathVariable Integer id,
            @RequestBody @Valid BudgetPlanDto request,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(budgetPlanService.updateBudgetPlan(id,request, connectedUser));
    }

}
