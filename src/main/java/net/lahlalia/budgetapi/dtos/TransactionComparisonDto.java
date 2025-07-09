package net.lahlalia.budgetapi.dtos;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionComparisonDto {

    List<TransactionDto> realTransactions;
    List<TransactionDto> expectedTransactions;

}
