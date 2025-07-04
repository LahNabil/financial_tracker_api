package net.lahlalia.budgetapi.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lahlalia.budgetapi.dtos.BudgetChartDataDto;
import net.lahlalia.budgetapi.dtos.BudgetPlanDto;
import net.lahlalia.budgetapi.dtos.PageResponse;
import net.lahlalia.budgetapi.entities.BudgetPlan;
import net.lahlalia.budgetapi.entities.Transaction;
import net.lahlalia.budgetapi.entities.User;
import net.lahlalia.budgetapi.enums.TransactionStatus;
import net.lahlalia.budgetapi.enums.TransactionType;
import net.lahlalia.budgetapi.mappers.BudgetPlanMapper;
import net.lahlalia.budgetapi.repositories.BudgetPlanRepository;
import net.lahlalia.budgetapi.repositories.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class BudgetPlanServiceImpl implements BudgetPlanService {

    private final BudgetPlanMapper budgetPlanMapper;
    private final BudgetPlanRepository budgetPlanRepository;
    private final TransactionRepository transactionRepository;

    public BudgetChartDataDto getCurrentMonthBudgetWithTransactions(Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        LocalDate now = LocalDate.now();
        int currentMonth = now.getMonthValue();
        int currentYear = now.getYear();

        log.info("Fetching budget chart data for user: {} for month: {}/{}",
                user.getEmail(), currentMonth, currentYear);

        // Find budget plan for current month/year
        BudgetPlan budgetPlan = budgetPlanRepository.findByUserIdAndMonthAndYear(
                        user.getId(), currentMonth, currentYear)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No budget plan found for current month (" + currentMonth + "/" + currentYear + ")"));

        // Get all transactions for this budget plan
        List<Transaction> allTransactions = transactionRepository
                .findTransactionsByBudgetPlanIdOrderByDate(budgetPlan.getId());

        // Filter and get amounts only
        List<BigDecimal> expectedTransactionAmounts = allTransactions.stream()
                .filter(t -> t.getStatus() == TransactionStatus.EXPECTED)
                .map(t -> t.getType() == TransactionType.EXPENSE ? t.getAmount().negate() : t.getAmount())
                .toList();

        List<BigDecimal> realTransactionAmounts = allTransactions.stream()
                .filter(t -> t.getStatus() == TransactionStatus.REAL)
                .map(t -> t.getType() == TransactionType.EXPENSE ? t.getAmount().negate() : t.getAmount())
                .toList();

        log.info("Found {} expected and {} real transactions for budget plan: {}",
                expectedTransactionAmounts.size(), realTransactionAmounts.size(), budgetPlan.getId());

        return BudgetChartDataDto.builder()
                .budgetPlan(budgetPlanMapper.toDto(budgetPlan))
                .expectedTransactions(expectedTransactionAmounts)
                .realTransactions(realTransactionAmounts)
                .build();
    }

    @Override
    public UUID findByMonthAndYear(int currentMonth, int currentYear, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        BudgetPlan budgetPlan = budgetPlanRepository.findByUserIdAndMonthAndYear(
                        user.getId(), currentMonth, currentYear)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No budget plan found for month: " + currentMonth + " and year: " + currentYear
                ));

        return budgetPlan.getId();
    }


    @Override
    public UUID save(BudgetPlanDto request, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        BudgetPlan budgetPlan = budgetPlanMapper.toEntity(request);
        budgetPlan.setUser(user);
        return budgetPlanRepository.save(budgetPlan).getId();
    }

    @Override
    public BudgetPlanDto findById(UUID id, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        BudgetPlan budgetPlan = budgetPlanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No budget Plan found with this ID"));
        if(!budgetPlan.getUser().getId().equals(user.getId())){
            throw new AccessDeniedException("You are not authorized to access this budget plan.");
        }
        return budgetPlanMapper.toDto(budgetPlan);

    }

    @Override
    public PageResponse<BudgetPlanDto> findAllBudgets(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BudgetPlan> budgetPlans = budgetPlanRepository.findAllByUserId(user.getId(), pageable);
        List<BudgetPlanDto> budgetPlanDtoList = budgetPlans.stream()
                .map(budgetPlanMapper::toDto)
                .toList();
        return new PageResponse<>(
                budgetPlanDtoList,
                budgetPlans.getNumber(),
                budgetPlans.getSize(),
                budgetPlans.getTotalElements(),
                budgetPlans.getTotalPages(),
                budgetPlans.isFirst(),
                budgetPlans.isLast()

        );
    }

    @Override
    public BudgetPlanDto updateBudgetPlan(UUID id, BudgetPlanDto request, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        BudgetPlan budgetPlan = budgetPlanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No Budget Plan found with this ID"));
        if (!budgetPlan.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not authorized to update this budget plan");
        }
        budgetPlan.setMonth(request.getMonth());
        budgetPlan.setInitialIncome(request.getInitialIncome());

        BudgetPlan updated = budgetPlanRepository.save(budgetPlan);
        return budgetPlanMapper.toDto(updated);
    }

    @Override
    public void deleteBudgetPlan(UUID id, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        BudgetPlan budgetPlan = budgetPlanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No Budget Plan found with this ID"));

        if (!budgetPlan.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not authorized to delete this budget plan");
        }
        budgetPlanRepository.delete(budgetPlan);

    }
}
