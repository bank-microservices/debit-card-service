package com.nttdata.microservices.debitcard.service.mapper;

import com.nttdata.microservices.debitcard.entity.DebitCard;
import com.nttdata.microservices.debitcard.service.dto.DebitCardDto;
import com.nttdata.microservices.debitcard.service.mapper.base.EntityMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DebitCardMapper extends EntityMapper<DebitCardDto, DebitCard> {

}
