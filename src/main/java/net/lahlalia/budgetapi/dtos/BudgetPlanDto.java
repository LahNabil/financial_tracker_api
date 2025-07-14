package net.lahlalia.budgetapi.dtos;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BudgetPlanDto {

    private UUID id;
    @NotNull(message = "Month is mandatory")
    @Positive(message = "Please Enter a valid month")
    @Min(value = 1, message = "Please Enter a valid month" )
    @Max(value = 12, message ="Please Enter a valid month" )
    private Integer month;
    @NotNull(message = "Year is mandatory ")
    @Positive(message = "Please Enter a valid Year")
    private Integer year;
    @NotNull(message = "Budget Intial amount is mandatory")
    @Positive(message = "Please Enter a valid Initial Amount")
    private BigDecimal initialIncome;
}
