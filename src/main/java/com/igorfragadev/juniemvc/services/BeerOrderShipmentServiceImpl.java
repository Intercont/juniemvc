package com.igorfragadev.juniemvc.services;

import com.igorfragadev.juniemvc.entities.BeerOrder;
import com.igorfragadev.juniemvc.entities.BeerOrderShipment;
import com.igorfragadev.juniemvc.mappers.BeerOrderShipmentMapper;
import com.igorfragadev.juniemvc.models.BeerOrderShipmentDto;
import com.igorfragadev.juniemvc.repositories.BeerOrderRepository;
import com.igorfragadev.juniemvc.repositories.BeerOrderShipmentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BeerOrderShipmentServiceImpl implements BeerOrderShipmentService {

    private final BeerOrderShipmentRepository beerOrderShipmentRepository;
    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderShipmentMapper beerOrderShipmentMapper;

    public BeerOrderShipmentServiceImpl(BeerOrderShipmentRepository beerOrderShipmentRepository,
                                       BeerOrderRepository beerOrderRepository,
                                       BeerOrderShipmentMapper beerOrderShipmentMapper) {
        this.beerOrderShipmentRepository = beerOrderShipmentRepository;
        this.beerOrderRepository = beerOrderRepository;
        this.beerOrderShipmentMapper = beerOrderShipmentMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BeerOrderShipmentDto> getAllBeerOrderShipments() {
        return beerOrderShipmentRepository.findAll().stream()
                .map(beerOrderShipmentMapper::beerOrderShipmentToBeerOrderShipmentDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BeerOrderShipmentDto> getShipmentsByBeerOrderId(Integer beerOrderId) {
        return beerOrderShipmentRepository.findByBeerOrderId(beerOrderId).stream()
                .map(beerOrderShipmentMapper::beerOrderShipmentToBeerOrderShipmentDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BeerOrderShipmentDto> getBeerOrderShipmentById(Integer id) {
        return beerOrderShipmentRepository.findById(id)
                .map(beerOrderShipmentMapper::beerOrderShipmentToBeerOrderShipmentDto);
    }

    @Override
    @Transactional
    public BeerOrderShipmentDto saveBeerOrderShipment(BeerOrderShipmentDto beerOrderShipmentDto) {
        BeerOrder beerOrder = beerOrderRepository.findById(beerOrderShipmentDto.getBeerOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Beer Order not found with ID: " + beerOrderShipmentDto.getBeerOrderId()));

        BeerOrderShipment beerOrderShipment = beerOrderShipmentMapper.beerOrderShipmentDtoToBeerOrderShipment(beerOrderShipmentDto);
        beerOrder.addBeerOrderShipment(beerOrderShipment);

        BeerOrderShipment savedBeerOrderShipment = beerOrderShipmentRepository.save(beerOrderShipment);
        return beerOrderShipmentMapper.beerOrderShipmentToBeerOrderShipmentDto(savedBeerOrderShipment);
    }

    @Override
    @Transactional
    public Optional<BeerOrderShipmentDto> updateBeerOrderShipment(Integer id, BeerOrderShipmentDto beerOrderShipmentDto) {
        return beerOrderShipmentRepository.findById(id)
                .map(existingBeerOrderShipment -> {
                    // If beer order ID has changed, update the relationship
                    if (!existingBeerOrderShipment.getBeerOrder().getId().equals(beerOrderShipmentDto.getBeerOrderId())) {
                        BeerOrder newBeerOrder = beerOrderRepository.findById(beerOrderShipmentDto.getBeerOrderId())
                                .orElseThrow(() -> new EntityNotFoundException("Beer Order not found with ID: " + beerOrderShipmentDto.getBeerOrderId()));
                        
                        // Update using the mapper helper method
                        beerOrderShipmentMapper.updateBeerOrderShipment(beerOrderShipmentDto, existingBeerOrderShipment, newBeerOrder);
                    } else {
                        // Update using the mapper helper method with null beerOrder to keep the existing one
                        beerOrderShipmentMapper.updateBeerOrderShipment(beerOrderShipmentDto, existingBeerOrderShipment, null);
                    }

                    return beerOrderShipmentRepository.save(existingBeerOrderShipment);
                })
                .map(beerOrderShipmentMapper::beerOrderShipmentToBeerOrderShipmentDto);
    }

    @Override
    @Transactional
    public boolean deleteBeerOrderShipment(Integer id) {
        if (beerOrderShipmentRepository.existsById(id)) {
            beerOrderShipmentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}