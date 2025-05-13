package net.lahlalia.budgetapi.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.lahlalia.budgetapi.dtos.PageResponse;
import net.lahlalia.budgetapi.dtos.TransactionDto;
import net.lahlalia.budgetapi.dtos.UpdateTransactionDto;
import net.lahlalia.budgetapi.services.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/{budgetPlanId}")
    public ResponseEntity<UUID> saveTransaction(
            @PathVariable UUID budgetPlanId,
            @Valid @RequestBody TransactionDto request,
            Authentication connectedUser
            ){
        request.setBudgetPlanId(budgetPlanId);
        UUID transactionId = transactionService.save(request, connectedUser);
        return ResponseEntity.ok(transactionId);
    }
    @GetMapping("/{id}")
    public ResponseEntity<TransactionDto> getTransactionById(
            @PathVariable UUID id,
            Authentication connectedUser) {
        TransactionDto transaction = transactionService.getTransactionById(id, connectedUser);
        return ResponseEntity.ok(transaction);
    }
    @GetMapping("/remaining-budget/{budgetId}")
    public ResponseEntity<BigDecimal> getRemainingBudget(@PathVariable UUID budgetId, Authentication authentication) {
        // Fetch the remaining budget by passing the budget ID and authentication details
        BigDecimal remainingBudget = transactionService.getRemainingBudget(budgetId, authentication);
        return ResponseEntity.ok(remainingBudget);  // Return the result with a 200 OK response
    }
    @GetMapping("/total-expense/{budgetId}")
    public ResponseEntity<BigDecimal> getTotalExpense(@PathVariable UUID budgetId, Authentication authentication) {
        // Fetch the total expense by passing the budget ID and authentication details
        BigDecimal totalExpense = transactionService.getTotalExpenseByBudget(budgetId, authentication);
        return ResponseEntity.ok(totalExpense);  // Return the result with a 200 OK response
    }

    @GetMapping("/total-income/{budgetId}")
    public ResponseEntity<BigDecimal> getTotalIncome(@PathVariable UUID budgetId, Authentication authentication) {
        // Fetch the total income by passing the budget ID and authentication details
        BigDecimal totalIncome = transactionService.getTotalIncomeByBudget(budgetId, authentication);
        return ResponseEntity.ok(totalIncome);  // Return the result with a 200 OK response
    }
    
    @GetMapping("/budget/{budgetId}")
    public ResponseEntity<PageResponse<TransactionDto>> findAllTransactionsByBudget(
            @PathVariable("budgetId") UUID budgetId,
            @RequestParam(name = "page", defaultValue = "0", required = false)int page,
            @RequestParam(name = "size", defaultValue = "10", required = false)int size,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(transactionService.findAllTransactionsByBudget(budgetId,page,size,connectedUser));
    }
    @PutMapping("/{id}")
    public ResponseEntity<TransactionDto> updateTransaction(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateTransactionDto request,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(transactionService.updateTransaction(id,request, connectedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(
            @PathVariable UUID id,
            Authentication connectedUser
    ){
        transactionService.deleteTransaction(id, connectedUser);
        return ResponseEntity.noContent().build();

    }
}
