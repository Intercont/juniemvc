package com.igorfragadev.juniemvc.models;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerOrderShipmentDto {
    // read only
    private Integer id;
    
    // read only
    private Integer version;

    @NotNull(message = "Beer order ID is required")
    private Integer beerOrderId;

    private LocalDate shipmentDate;

    private String carrier;

    private String trackingNumber;

    // read only
    private LocalDateTime createdDate;
    
    // read only
    private LocalDateTime updatedDate;
}