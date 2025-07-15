package com.igorfragadev.juniemvc.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerOrderDto {
    //read only
    private Integer id;
    //read only
    private Integer version;
    
    @NotBlank(message = "Customer reference is required")
    private String customerRef;
    
    @NotNull(message = "Payment amount is required")
    @Positive(message = "Payment amount must be positive")
    private BigDecimal paymentAmount;
    
    private String status;

    //read only
    private LocalDateTime createdDate;
    //read only
    private LocalDateTime updatedDate;
    
    @Builder.Default
    private List<BeerOrderLineDto> beerOrderLines = new ArrayList<>();
}