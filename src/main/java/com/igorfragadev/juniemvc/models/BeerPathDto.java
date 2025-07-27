package com.igorfragadev.juniemvc.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * DTO for PATCH operations on Beer entity.
 * Contains the same properties as the Beer entity without any constraints,
 * and ignores createdDate and updatedDate properties.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerPathDto {
    private Integer id;
    private Integer version;
    private String beerName;
    private String beerStyle;
    private String upc;
    private Integer quantityOnHand;
    private BigDecimal price;
    private String imageUrl;
}