package net.lahlalia.budgetapi.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordRequest {

    private String oldPassword;
    private String newPassword;
}
