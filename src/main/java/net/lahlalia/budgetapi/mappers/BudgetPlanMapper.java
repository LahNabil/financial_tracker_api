package net.lahlalia.budgetapi.mappers;
import lombok.RequiredArgsConstructor;
import net.lahlalia.budgetapi.dtos.BudgetPlanDto;
import net.lahlalia.budgetapi.entities.BudgetPlan;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BudgetPlanMapper {
    private final ModelMapper mapper;

    public BudgetPlan toEntity(BudgetPlanDto dto){
        return mapper.map(dto, BudgetPlan.class);

    }
    public BudgetPlanDto toDto(BudgetPlan budgetPlan){
        return mapper.map(budgetPlan, BudgetPlanDto.class);
    }
}
