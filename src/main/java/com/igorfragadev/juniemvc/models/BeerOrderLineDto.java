package com.igorfragadev.juniemvc.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerOrderLineDto {
    //read only
    private Integer id;
    //read only
    private Integer version;
    
    @NotNull(message = "Beer ID is required")
    private Integer beerId;
    
    @NotNull(message = "Order quantity is required")
    @Positive(message = "Order quantity must be positive")
    private Integer orderQuantity;
    
    private Integer quantityAllocated;
    private String status;

    //read only
    private LocalDateTime createdDate;
    //read only
    private LocalDateTime updatedDate;
    
    // Optional: Include beer details for display purposes
    private BeerDto beer;
}