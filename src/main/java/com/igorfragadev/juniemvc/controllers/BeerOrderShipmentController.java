package com.igorfragadev.juniemvc.controllers;

import com.igorfragadev.juniemvc.models.BeerOrderShipmentDto;
import com.igorfragadev.juniemvc.services.BeerOrderShipmentService;
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
@RequestMapping("/api/v1/beer-order-shipments")
public class BeerOrderShipmentController {

    private final BeerOrderShipmentService beerOrderShipmentService;

    public BeerOrderShipmentController(BeerOrderShipmentService beerOrderShipmentService) {
        this.beerOrderShipmentService = beerOrderShipmentService;
    }

    @GetMapping
    public ResponseEntity<List<BeerOrderShipmentDto>> getAllBeerOrderShipments() {
        return new ResponseEntity<>(beerOrderShipmentService.getAllBeerOrderShipments(), HttpStatus.OK);
    }

    @GetMapping("/beer-order/{beerOrderId}")
    public ResponseEntity<List<BeerOrderShipmentDto>> getShipmentsByBeerOrderId(@PathVariable("beerOrderId") Integer beerOrderId) {
        return new ResponseEntity<>(beerOrderShipmentService.getShipmentsByBeerOrderId(beerOrderId), HttpStatus.OK);
    }

    @GetMapping("/{shipmentId}")
    public ResponseEntity<BeerOrderShipmentDto> getBeerOrderShipmentById(@PathVariable("shipmentId") Integer shipmentId) {
        return beerOrderShipmentService.getBeerOrderShipmentById(shipmentId)
                .map(shipment -> new ResponseEntity<>(shipment, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<BeerOrderShipmentDto> createBeerOrderShipment(@Valid @RequestBody BeerOrderShipmentDto beerOrderShipmentDto) {
        BeerOrderShipmentDto savedShipment = beerOrderShipmentService.saveBeerOrderShipment(beerOrderShipmentDto);
        return new ResponseEntity<>(savedShipment, HttpStatus.CREATED);
    }

    @PutMapping("/{shipmentId}")
    public ResponseEntity<BeerOrderShipmentDto> updateBeerOrderShipment(
            @PathVariable("shipmentId") Integer shipmentId,
            @Valid @RequestBody BeerOrderShipmentDto beerOrderShipmentDto) {
        return beerOrderShipmentService.updateBeerOrderShipment(shipmentId, beerOrderShipmentDto)
                .map(updatedShipment -> new ResponseEntity<>(updatedShipment, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{shipmentId}")
    public ResponseEntity<Void> deleteBeerOrderShipment(@PathVariable("shipmentId") Integer shipmentId) {
        if (beerOrderShipmentService.deleteBeerOrderShipment(shipmentId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}