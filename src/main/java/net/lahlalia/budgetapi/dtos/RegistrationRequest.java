package net.lahlalia.budgetapi.dtos;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegistrationRequest {

    @NotEmpty(message = "firstname is mandatory")
    @NotBlank(message = "firstname is mandatory")
    private String firstname;

    @NotEmpty(message = "lastname is mandatory")
    @NotBlank(message = "lastname is mandatory")
    private String lastname;

    @NotEmpty(message = "email is mandatory")
    @NotBlank(message = "email is mandatory")
    @Email(message = "email is not correct")
    private String email;

    @NotEmpty(message = "password is mandatory")
    @NotBlank(message = "password is mandatory")
    @Size(min= 8, message = "password should be 8 characters minimum")
    private String password;
}
