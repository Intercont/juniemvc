package com.igorfragadev.juniemvc.mappers;

import com.igorfragadev.juniemvc.entities.Beer;
import com.igorfragadev.juniemvc.models.BeerDto;
import com.igorfragadev.juniemvc.models.BeerPathDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface BeerMapper {
    @Mapping(target = "imageUrl", source = "imageUrl")
    BeerDto beerToBeerDto(Beer beer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "imageUrl", source = "imageUrl")
    Beer beerDtoToBeer(BeerDto beerDto);

    /**
     * Updates a Beer entity with non-null values from a BeerPathDto.
     * 
     * @param beerPathDto the DTO containing the updated values
     * @param beer the Beer entity to update
     * @return the updated Beer entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "beerOrderLines", ignore = true)
    @Mapping(target = "imageUrl", source = "imageUrl", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "beerName", source = "beerName", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "beerStyle", source = "beerStyle", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "upc", source = "upc", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "quantityOnHand", source = "quantityOnHand", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "price", source = "price", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Beer updateBeerFromBeerPathDto(BeerPathDto beerPathDto, @MappingTarget Beer beer);
}
