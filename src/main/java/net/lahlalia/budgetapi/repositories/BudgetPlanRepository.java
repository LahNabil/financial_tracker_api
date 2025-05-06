package net.lahlalia.budgetapi.repositories;

import net.lahlalia.budgetapi.entities.BudgetPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BudgetPlanRepository extends JpaRepository<BudgetPlan, UUID> {

    Page<BudgetPlan> findAllByUserId(UUID userId, Pageable pageable);
}
