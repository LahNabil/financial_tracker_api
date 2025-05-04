package net.lahlalia.budgetapi.repositories;

import net.lahlalia.budgetapi.entities.BudgetPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetPlanRepository extends JpaRepository<BudgetPlan, Integer> {
}
