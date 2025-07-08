package com.igorfragadev.juniemvc.services;

import com.igorfragadev.juniemvc.entities.Beer;
import com.igorfragadev.juniemvc.entities.BeerOrder;
import com.igorfragadev.juniemvc.entities.BeerOrderLine;
import com.igorfragadev.juniemvc.mappers.BeerOrderMapper;
import com.igorfragadev.juniemvc.models.BeerOrderDto;
import com.igorfragadev.juniemvc.models.BeerOrderLineDto;
import com.igorfragadev.juniemvc.repositories.BeerOrderRepository;
import com.igorfragadev.juniemvc.repositories.BeerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BeerOrderServiceImpl implements BeerOrderService {

    private final BeerOrderRepository beerOrderRepository;
    private final BeerRepository beerRepository;
    private final BeerOrderMapper beerOrderMapper;

    public BeerOrderServiceImpl(BeerOrderRepository beerOrderRepository, 
                               BeerRepository beerRepository,
                               BeerOrderMapper beerOrderMapper) {
        this.beerOrderRepository = beerOrderRepository;
        this.beerRepository = beerRepository;
        this.beerOrderMapper = beerOrderMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BeerOrderDto> getAllBeerOrders() {
        return beerOrderRepository.findAll().stream()
                .map(beerOrderMapper::beerOrderToBeerOrderDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BeerOrderDto> getBeerOrderById(Integer id) {
        return beerOrderRepository.findById(id)
                .map(beerOrderMapper::beerOrderToBeerOrderDto);
    }

    @Override
    @Transactional
    public BeerOrderDto saveBeerOrder(BeerOrderDto beerOrderDto) {
        BeerOrder beerOrder = beerOrderMapper.beerOrderDtoToBeerOrder(beerOrderDto);
        
        // Process beer order lines
        if (beerOrderDto.getBeerOrderLines() != null) {
            beerOrderDto.getBeerOrderLines().forEach(lineDto -> {
                Beer beer = beerRepository.findById(lineDto.getBeerId())
                    .orElseThrow(() -> new EntityNotFoundException("Beer not found with ID: " + lineDto.getBeerId()));
                
                BeerOrderLine line = BeerOrderLine.builder()
                    .beer(beer)
                    .orderQuantity(lineDto.getOrderQuantity())
                    .quantityAllocated(0) // Default to 0 for new orders
                    .status("NEW")
                    .build();
                
                beerOrder.addBeerOrderLine(line);
            });
        }
        
        BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);
        return beerOrderMapper.beerOrderToBeerOrderDto(savedBeerOrder);
    }

    @Override
    @Transactional
    public Optional<BeerOrderDto> updateBeerOrder(Integer id, BeerOrderDto beerOrderDto) {
        return beerOrderRepository.findById(id)
                .map(existingBeerOrder -> {
                    // Update basic fields
                    existingBeerOrder.setCustomerRef(beerOrderDto.getCustomerRef());
                    existingBeerOrder.setPaymentAmount(beerOrderDto.getPaymentAmount());
                    existingBeerOrder.setStatus(beerOrderDto.getStatus());
                    
                    // Handle order lines updates if needed
                    // This is a simplified approach - in a real application, you might need
                    // more sophisticated logic to handle adding/removing/updating lines
                    
                    return beerOrderRepository.save(existingBeerOrder);
                })
                .map(beerOrderMapper::beerOrderToBeerOrderDto);
    }

    @Override
    @Transactional
    public boolean deleteBeerOrder(Integer id) {
        if (beerOrderRepository.existsById(id)) {
            beerOrderRepository.deleteById(id);
            return true;
        }
        return false;
    }
}