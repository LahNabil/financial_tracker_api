package net.lahlalia.budgetapi.mappers;

import lombok.RequiredArgsConstructor;
import net.lahlalia.budgetapi.dtos.TransactionDto;
import net.lahlalia.budgetapi.entities.Transaction;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.Mapping;

@Component
@RequiredArgsConstructor
public class TransactionMapper {
    private final ModelMapper mapper;


    public Transaction toEntity(TransactionDto dto){
        return mapper.map(dto, Transaction.class);

    }
    public TransactionDto toDto(Transaction transaction){
        return mapper.map(transaction, TransactionDto.class);
    }


}
