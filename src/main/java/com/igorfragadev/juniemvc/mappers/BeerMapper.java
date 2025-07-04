package com.igorfragadev.juniemvc.mappers;

import com.igorfragadev.juniemvc.entities.Beer;
import com.igorfragadev.juniemvc.models.BeerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BeerMapper {
    BeerDto beerToBeerDto(Beer beer);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    Beer beerDtoToBeer(BeerDto beerDto);
}