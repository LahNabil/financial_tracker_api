package net.lahlalia.budgetapi.web;

import lombok.RequiredArgsConstructor;
import net.lahlalia.budgetapi.dtos.DashboardResponseDto;
import net.lahlalia.budgetapi.dtos.TransactionComparisonDto;
import net.lahlalia.budgetapi.services.DashboardService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping(value="/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DashboardResponseDto> GetDashboard(
            @RequestParam(required = false) UUID budgetId,
            Authentication connectedUser
            ){
        var data = dashboardService.getDashboardData(budgetId, connectedUser);
        return ResponseEntity.ok(data);
    }
    @GetMapping(value="/line", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionComparisonDto> getTransactionComparison(
            @RequestParam(required = false) UUID budgetId,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(dashboardService.getTransactionComparison(budgetId, connectedUser));
    }

}
