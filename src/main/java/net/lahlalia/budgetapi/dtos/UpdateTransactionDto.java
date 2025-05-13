package net.lahlalia.budgetapi.dtos;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import net.lahlalia.budgetapi.enums.TransactionCategory;
import net.lahlalia.budgetapi.enums.TransactionStatus;
import net.lahlalia.budgetapi.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateTransactionDto {


    private UUID id;
    @NotNull(message = "200")
    @NotEmpty(message = "201")
    @NotBlank(message = "201")
    String title;
    @Positive(message = "202")
    @NotNull(message = "203")
    BigDecimal amount;
    LocalDate date;
    @NotNull(message = "204")
    @Enumerated(EnumType.STRING)
    TransactionStatus status;
    @NotNull(message = "205")
    @Enumerated(EnumType.STRING)
    TransactionCategory category;
    @NotNull(message = "206")
    @Enumerated(EnumType.STRING)
    TransactionType type;
    String description;
    UUID BudgetPlanId;

}


