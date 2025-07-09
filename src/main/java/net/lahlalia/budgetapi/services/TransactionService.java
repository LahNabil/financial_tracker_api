package net.lahlalia.budgetapi.services;

import net.lahlalia.budgetapi.dtos.CategorySummaryDto;
import net.lahlalia.budgetapi.dtos.PageResponse;
import net.lahlalia.budgetapi.dtos.TransactionDto;
import net.lahlalia.budgetapi.dtos.UpdateTransactionDto;
import net.lahlalia.budgetapi.enums.TransactionStatus;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface TransactionService {
    UUID save(TransactionDto request, Authentication connectedUser);

    PageResponse<TransactionDto> findAllTransactionsByBudget(UUID budgetId, int page, int size, Authentication connectedUser);

    TransactionDto updateTransaction(UUID id, UpdateTransactionDto request, Authentication connectedUser);

    void deleteTransaction(UUID id, Authentication connectedUser);
    TransactionDto getTransactionById(UUID id, Authentication connectedUser);
    BigDecimal getTotalExpenseByBudget(UUID budgetId, Authentication connectedUser);
    BigDecimal getTotalIncomeByBudget(UUID budgetId, Authentication connectedUser);
    BigDecimal getRemainingBudget(UUID budgetId, Authentication connectedUser);

    List<CategorySummaryDto> getExpensesByCategory(UUID budgetId, Authentication connectedUser);
    List<TransactionDto> findTransactionsByStatus(UUID budgetId, TransactionStatus status, Authentication connectedUser);


}
