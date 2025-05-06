package net.lahlalia.budgetapi.services;

import net.lahlalia.budgetapi.dtos.PageResponse;
import net.lahlalia.budgetapi.dtos.TransactionDto;
import net.lahlalia.budgetapi.dtos.UpdateTransactionDto;
import org.springframework.security.core.Authentication;

import java.util.UUID;

public interface TransactionService {
    UUID save(TransactionDto request, Authentication connectedUser);

    PageResponse<TransactionDto> findAllTransactionsByBudget(UUID budgetId, int page, int size, Authentication connectedUser);

    TransactionDto updateTransaction(UUID id, UpdateTransactionDto request, Authentication connectedUser);

    void deleteTransaction(UUID id, Authentication connectedUser);
}
