package net.lahlalia.budgetapi.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BudgetPlanDto {

    Integer id;
    @NotNull(message = "100")
    @NotEmpty(message = "100")
    private String month;
    @NotNull(message = "101")
    private BigDecimal initialIncome;
}
