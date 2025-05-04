package net.lahlalia.budgetapi.services;

import net.lahlalia.budgetapi.dtos.TransactionDto;
import org.springframework.security.core.Authentication;

public interface TransactionService {
    Integer save(TransactionDto request, Authentication connectedUser);
}
