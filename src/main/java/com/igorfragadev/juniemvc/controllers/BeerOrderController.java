package com.igorfragadev.juniemvc.controllers;

import com.igorfragadev.juniemvc.models.BeerOrderDto;
import com.igorfragadev.juniemvc.services.BeerOrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/beer-orders")
public class BeerOrderController {

    private final BeerOrderService beerOrderService;

    public BeerOrderController(BeerOrderService beerOrderService) {
        this.beerOrderService = beerOrderService;
    }

    @GetMapping
    public ResponseEntity<List<BeerOrderDto>> getAllBeerOrders() {
        return new ResponseEntity<>(beerOrderService.getAllBeerOrders(), HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<BeerOrderDto> getBeerOrderById(@PathVariable("orderId") Integer orderId) {
        return beerOrderService.getBeerOrderById(orderId)
                .map(order -> new ResponseEntity<>(order, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<BeerOrderDto> createBeerOrder(@Valid @RequestBody BeerOrderDto beerOrderDto) {
        BeerOrderDto savedOrder = beerOrderService.saveBeerOrder(beerOrderDto);
        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<BeerOrderDto> updateBeerOrder(
            @PathVariable("orderId") Integer orderId,
            @Valid @RequestBody BeerOrderDto beerOrderDto) {
        return beerOrderService.updateBeerOrder(orderId, beerOrderDto)
                .map(updatedOrder -> new ResponseEntity<>(updatedOrder, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteBeerOrder(@PathVariable("orderId") Integer orderId) {
        if (beerOrderService.deleteBeerOrder(orderId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}