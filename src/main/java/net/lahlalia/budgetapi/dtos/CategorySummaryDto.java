package net.lahlalia.budgetapi.dtos;

import lombok.*;
import net.lahlalia.budgetapi.enums.TransactionCategory;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategorySummaryDto {
    private TransactionCategory category;
    private BigDecimal total;


}
