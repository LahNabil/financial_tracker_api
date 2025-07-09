package net.lahlalia.budgetapi.repositories;

import net.lahlalia.budgetapi.dtos.CategorySummaryDto;
import net.lahlalia.budgetapi.entities.Transaction;
import net.lahlalia.budgetapi.enums.TransactionStatus;
import net.lahlalia.budgetapi.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    @Query("""
SELECT transaction from Transaction transaction WHERE transaction.budgetPlan.id = :budgetId
""")
    Page<Transaction> findAllByBudgetId(UUID budgetId, Pageable pageable);


    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.budgetPlan.id = :budgetId AND t.type = :type AND t.status = :status")
    Optional<BigDecimal> sumAmountByBudgetIdAndTypeAndStatus(
            @Param("budgetId") UUID budgetId,
            @Param("type") TransactionType type,
            @Param("status") TransactionStatus status);



    @Query("SELECT t FROM Transaction t WHERE t.budgetPlan.id = :budgetPlanId ORDER BY t.date ASC")
    List<Transaction> findTransactionsByBudgetPlanIdOrderByDate(@Param("budgetPlanId") UUID budgetPlanId);

    @Query("""
    SELECT new net.lahlalia.budgetapi.dtos.CategorySummaryDto(t.category, SUM(t.amount))
    FROM Transaction t
    WHERE t.budgetPlan.id = :budgetId AND t.type = 'EXPENSE' AND t.status = "REAL"
    GROUP BY t.category
    """)
    List<CategorySummaryDto> getExpensesByCategory(@Param("budgetId") UUID budgetId);


    @Query("SELECT t FROM Transaction t WHERE t.budgetPlan.id = :budgetId AND t.status = 'REAL'")
    List<Transaction> findRealTransactionsByBudget(@Param("budgetId") UUID budgetId);

    @Query("SELECT t FROM Transaction t WHERE t.budgetPlan.id = :budgetId AND t.status = 'EXPECTED'")
    List<Transaction> findExpectedTransactionsByBudget(@Param("budgetId") UUID budgetId);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.budgetPlan.id = :budgetId AND t.status = 'REAL' AND t.type = 'INCOME'")
    BigDecimal sumRealIncome(@Param("budgetId") UUID budgetId);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.budgetPlan.id = :budgetId AND t.status = 'EXPECTED' AND t.type = 'INCOME'")
    BigDecimal sumExpectedIncome(@Param("budgetId") UUID budgetId);



}
