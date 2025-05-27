package net.lahlalia.budgetapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BudgetChartDataDto {
    private BudgetPlanDto budgetPlan;
    private List<BigDecimal> expectedTransactions;
    private List<BigDecimal> realTransactions;

}
