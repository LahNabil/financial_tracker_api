package net.lahlalia.budgetapi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.lahlalia.budgetapi.enums.TransactionCategory;
import net.lahlalia.budgetapi.enums.TransactionStatus;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private BigDecimal amount;
    private LocalDate date;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    @Enumerated(EnumType.STRING)
    private TransactionCategory category;

    @ManyToOne
    @JoinColumn(name = "budgetPlan_id")
    private BudgetPlan budgetPlan;


    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;
    @CreatedBy
    @Column(nullable = false, updatable = false)
    private Integer createdBy;
    @LastModifiedBy
    @Column(insertable = false)
    private Integer lastModifiedBy;

}
