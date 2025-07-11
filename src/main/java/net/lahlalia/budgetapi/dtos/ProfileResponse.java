package net.lahlalia.budgetapi.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {
    private String firstname;
    private String lastname;
    private String email;
}
