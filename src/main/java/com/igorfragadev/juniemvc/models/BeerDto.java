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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerDto {
    private Integer id;
    private Integer version;

    @NotBlank(message = "Beer name is required")
    private String beerName;

    // style of the beer, ALE, PALE ALE, IPA, etc
    @NotBlank(message = "Beer style is required")
    private String beerStyle;

    // Universal Product Code, a 13-digit number assigned to each unique beer product by the Federal Bar Association
    @NotBlank(message = "UPC is required")
    private String upc;

    @NotNull(message = "Quantity on hand is required")
    @Positive(message = "Quantity on hand must be positive")
    private Integer quantityOnHand;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    private String imageUrl;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
