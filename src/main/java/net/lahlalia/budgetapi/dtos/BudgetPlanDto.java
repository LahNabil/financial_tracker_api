package net.lahlalia.budgetapi.dtos;

import jakarta.validation.constraints.*;
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
    @Positive(message = "101")
    @Min(value = 1, message = "102" )
    @Max(value = 12, message ="103" )
    private Integer month;
    @NotNull(message = "104")
    @Positive(message = "105")
    private Integer year;
    @NotNull(message = "106")
    @Positive(message = "107")
    private BigDecimal initialIncome;
}
