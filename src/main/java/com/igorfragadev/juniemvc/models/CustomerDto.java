package com.igorfragadev.juniemvc.models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDto {
    // read only
    private Integer id;
    // read only
    private Integer version;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    private String email;
    private String phone;
    
    @NotBlank(message = "Address line 1 is required")
    private String addressLine1;
    
    private String addressLine2;
    
    @NotBlank(message = "City is required")
    private String city;
    
    @NotBlank(message = "State is required")
    private String state;
    
    @NotBlank(message = "Postal code is required")
    private String postalCode;
    
    // read only
    private LocalDateTime createdAt;
    // read only
    private LocalDateTime updatedAt;
    
    @Builder.Default
    private List<BeerOrderDto> beerOrders = new ArrayList<>();
}