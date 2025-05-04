package net.lahlalia.budgetapi.repositories;

import net.lahlalia.budgetapi.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}
