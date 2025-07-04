package net.lahlalia.budgetapi.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.lahlalia.budgetapi.dtos.BudgetChartDataDto;
import net.lahlalia.budgetapi.dtos.BudgetPlanDto;
import net.lahlalia.budgetapi.dtos.PageResponse;
import net.lahlalia.budgetapi.services.BudgetPlanService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/budget", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class BudgetPlanController {

    private final BudgetPlanService budgetPlanService;

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UUID> saveBudgetPlan(
            @Valid @RequestBody BudgetPlanDto request,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(budgetPlanService.save(request, connectedUser));
    }

    @GetMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BudgetPlanDto> findBudgetPlanById(
            @PathVariable("id") UUID id,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(budgetPlanService.findById(id, connectedUser));
    }

    @GetMapping(value="/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponse<BudgetPlanDto>> findAllBudgets(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(budgetPlanService.findAllBudgets(page, size, connectedUser));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BudgetPlanDto> updateBudgetPlan(
            @PathVariable UUID id,
            @RequestBody @Valid BudgetPlanDto request,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(budgetPlanService.updateBudgetPlan(id, request, connectedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudgetPlan(
            @PathVariable UUID id,
            Authentication connectedUser
    ) {
        budgetPlanService.deleteBudgetPlan(id, connectedUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value="/current-month/with-transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BudgetChartDataDto> getCurrentMonthBudgetWithTransactions(
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(budgetPlanService.getCurrentMonthBudgetWithTransactions(connectedUser));
    }
}