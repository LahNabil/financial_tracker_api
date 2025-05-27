package net.lahlalia.budgetapi.repositories;

import net.lahlalia.budgetapi.entities.BudgetPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface BudgetPlanRepository extends JpaRepository<BudgetPlan, UUID> {

    Page<BudgetPlan> findAllByUserId(UUID userId, Pageable pageable);

    // Option 2: Using @Query annotation (if you prefer custom queries)
    @Query("SELECT bp FROM BudgetPlan bp WHERE bp.user.id = :userId AND bp.month = :month AND bp.year = :year")
    Optional<BudgetPlan> findByUserIdAndMonthAndYear(@Param("userId") UUID userId,
                                                     @Param("month") Integer month,
                                                     @Param("year") Integer year);
}
