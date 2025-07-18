package com.igorfragadev.juniemvc.mappers;

import com.igorfragadev.juniemvc.entities.BeerOrder;
import com.igorfragadev.juniemvc.entities.BeerOrderShipment;
import com.igorfragadev.juniemvc.models.BeerOrderShipmentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BeerOrderShipmentMapper {
    @Mapping(target = "beerOrderId", source = "beerOrder.id")
    BeerOrderShipmentDto beerOrderShipmentToBeerOrderShipmentDto(BeerOrderShipment beerOrderShipment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "beerOrder", ignore = true)
    BeerOrderShipment beerOrderShipmentDtoToBeerOrderShipment(BeerOrderShipmentDto beerOrderShipmentDto);

    default BeerOrderShipment updateBeerOrderShipment(BeerOrderShipmentDto dto, BeerOrderShipment shipment, BeerOrder beerOrder) {
        if (dto == null) {
            return shipment;
        }

        shipment.setShipmentDate(dto.getShipmentDate());
        shipment.setCarrier(dto.getCarrier());
        shipment.setTrackingNumber(dto.getTrackingNumber());
        
        if (beerOrder != null) {
            shipment.setBeerOrder(beerOrder);
        }
        
        return shipment;
    }
}