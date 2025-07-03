package net.lahlalia.budgetapi.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.lahlalia.budgetapi.dtos.PageResponse;
import net.lahlalia.budgetapi.dtos.TransactionDto;
import net.lahlalia.budgetapi.dtos.UpdateTransactionDto;
import net.lahlalia.budgetapi.services.TransactionService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/transaction", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping(value = "/{budgetPlanId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UUID> saveTransaction(
            @PathVariable UUID budgetPlanId,
            @Valid @RequestBody TransactionDto request,
            Authentication connectedUser
    ) {
        request.setBudgetPlanId(budgetPlanId);
        UUID transactionId = transactionService.save(request, connectedUser);
        return ResponseEntity.ok(transactionId);
    }

    @GetMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionDto> getTransactionById(
            @PathVariable UUID id,
            Authentication connectedUser) {
        TransactionDto transaction = transactionService.getTransactionById(id, connectedUser);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping(value="/remaining-budget/{budgetId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BigDecimal> getRemainingBudget(
            @PathVariable UUID budgetId,
            Authentication authentication) {
        BigDecimal remainingBudget = transactionService.getRemainingBudget(budgetId, authentication);
        return ResponseEntity.ok(remainingBudget);
    }

    @GetMapping(value = "/total-expense/{budgetId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BigDecimal> getTotalExpense(
            @PathVariable UUID budgetId,
            Authentication authentication) {
        BigDecimal totalExpense = transactionService.getTotalExpenseByBudget(budgetId, authentication);
        return ResponseEntity.ok(totalExpense);
    }

    @GetMapping(value="/total-income/{budgetId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BigDecimal> getTotalIncome(
            @PathVariable UUID budgetId,
            Authentication authentication) {
        BigDecimal totalIncome = transactionService.getTotalIncomeByBudget(budgetId, authentication);
        return ResponseEntity.ok(totalIncome);
    }

    @GetMapping(value="/budget/{budgetId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponse<TransactionDto>> findAllTransactionsByBudget(
            @PathVariable("budgetId") UUID budgetId,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(transactionService.findAllTransactionsByBudget(budgetId, page, size, connectedUser));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionDto> updateTransaction(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateTransactionDto request,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(transactionService.updateTransaction(id, request, connectedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(
            @PathVariable UUID id,
            Authentication connectedUser
    ) {
        transactionService.deleteTransaction(id, connectedUser);
        return ResponseEntity.noContent().build();
    }
}