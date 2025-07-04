package com.igorfragadev.juniemvc.mappers;

import com.igorfragadev.juniemvc.entities.BeerOrder;
import com.igorfragadev.juniemvc.models.BeerOrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BeerOrderLineMapper.class})
public interface BeerOrderMapper {
    BeerOrderDto beerOrderToBeerOrderDto(BeerOrder beerOrder);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    BeerOrder beerOrderDtoToBeerOrder(BeerOrderDto beerOrderDto);
}