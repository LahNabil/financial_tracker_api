package net.lahlalia.budgetapi.repositories;

import net.lahlalia.budgetapi.entities.BudgetPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetPlanRepository extends JpaRepository<BudgetPlan, Integer> {

    Page<BudgetPlan> findAllByUserId(Integer userId, Pageable pageable);
}
