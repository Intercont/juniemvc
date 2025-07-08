package com.igorfragadev.juniemvc.mappers;

import com.igorfragadev.juniemvc.entities.BeerOrderLine;
import com.igorfragadev.juniemvc.models.BeerOrderLineDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BeerMapper.class})
public interface BeerOrderLineMapper {
    @Mapping(target = "beerId", source = "beer.id")
    BeerOrderLineDto beerOrderLineToBeerOrderLineDto(BeerOrderLine beerOrderLine);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "beer", ignore = true)
    @Mapping(target = "beerOrder", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    BeerOrderLine beerOrderLineDtoToBeerOrderLine(BeerOrderLineDto beerOrderLineDto);
}