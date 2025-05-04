package net.lahlalia.budgetapi.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.lahlalia.budgetapi.dtos.AuthenticationRequest;
import net.lahlalia.budgetapi.dtos.AuthenticationResponse;
import net.lahlalia.budgetapi.dtos.RegistrationRequest;
import net.lahlalia.budgetapi.services.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(
            @RequestBody @Valid RegistrationRequest request
    ){
        authenticationService.register(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest request
    ){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
