package net.lahlalia.budgetapi.repositories;

import net.lahlalia.budgetapi.entities.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    @Query("""
SELECT transaction from Transaction transaction WHERE transaction.budgetPlan.id = :budgetId
""")
    Page<Transaction> findAllByBudgetId(UUID budgetId, Pageable pageable);
}
