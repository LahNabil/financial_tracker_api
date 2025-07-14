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
public class TransactionDto {

    private UUID id;
    @NotNull(message = "Title Mandatory")
    @NotEmpty(message = "Title Mandatory")
    @NotBlank(message = "Title Mandatory")
    String title;
    @Positive(message = "Please Enter a Positive Value")
    @NotNull(message = "Amount is mandatory")
    BigDecimal amount;
    LocalDate date;
    @NotNull(message = "Status is mandatory")
    @Enumerated(EnumType.STRING)
    TransactionStatus status;
    @NotNull(message = "Category is mandatory")
    @Enumerated(EnumType.STRING)
    TransactionCategory category;
    @NotNull(message = "Type is Mandatory")
    @Enumerated(EnumType.STRING)
    TransactionType type;
    String description;
    @NotNull(message = "207")
    UUID BudgetPlanId;

}
