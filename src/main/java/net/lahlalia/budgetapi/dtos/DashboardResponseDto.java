package net.lahlalia.budgetapi.dtos;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponseDto {

    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private List<CategorySummaryDto> expensesByCategory;
}
