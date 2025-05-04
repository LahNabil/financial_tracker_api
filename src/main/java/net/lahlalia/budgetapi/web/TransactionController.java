package net.lahlalia.budgetapi.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.lahlalia.budgetapi.dtos.TransactionDto;
import net.lahlalia.budgetapi.services.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/")
    public ResponseEntity<Integer> saveTransaction(
            @Valid @RequestBody TransactionDto request,
            Authentication connectedUser
            ){
        return ResponseEntity.ok(transactionService.save(request,connectedUser));
    }
}
