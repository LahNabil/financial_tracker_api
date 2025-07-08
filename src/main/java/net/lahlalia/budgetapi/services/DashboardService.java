package net.lahlalia.budgetapi.services;

import net.lahlalia.budgetapi.dtos.DashboardResponseDto;
import net.lahlalia.budgetapi.dtos.TransactionComparisonDto;
import org.springframework.security.core.Authentication;

import java.util.UUID;

public interface DashboardService {

    DashboardResponseDto getDashboardData(UUID budgetId, Authentication connectedUser);
    public TransactionComparisonDto getTransactionComparison(UUID budgetId, Authentication connectedUser);
}
