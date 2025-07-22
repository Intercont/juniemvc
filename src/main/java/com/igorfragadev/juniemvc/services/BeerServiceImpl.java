package com.igorfragadev.juniemvc.services;

import com.igorfragadev.juniemvc.entities.Beer;
import com.igorfragadev.juniemvc.mappers.BeerMapper;
import com.igorfragadev.juniemvc.models.BeerDto;
import com.igorfragadev.juniemvc.repositories.BeerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    public BeerServiceImpl(BeerRepository beerRepository, BeerMapper beerMapper) {
        this.beerRepository = beerRepository;
        this.beerMapper = beerMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BeerDto> getAllBeers() {
        return beerRepository.findAll().stream()
                .map(beerMapper::beerToBeerDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BeerDto> getAllBeers(String beerName, String beerStyle, Pageable pageable) {
        Page<Beer> beerPage;

        boolean hasName = StringUtils.hasText(beerName);
        boolean hasStyle = StringUtils.hasText(beerStyle);

        if (hasName && hasStyle) {
            // Both name and style are provided
            beerPage = beerRepository.findAllByBeerNameContainingIgnoreCaseAndBeerStyleContainingIgnoreCase(
                    beerName, beerStyle, pageable);
        } else if (hasName) {
            // Only name is provided
            beerPage = beerRepository.findAllByBeerNameContainingIgnoreCase(beerName, pageable);
        } else if (hasStyle) {
            // Only style is provided
            beerPage = beerRepository.findAllByBeerStyleContainingIgnoreCase(beerStyle, pageable);
        } else {
            // Neither name nor style is provided
            beerPage = beerRepository.findAll(pageable);
        }

        return beerPage.map(beerMapper::beerToBeerDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BeerDto> getBeerById(Integer id) {
        return beerRepository.findById(id)
                .map(beerMapper::beerToBeerDto);
    }

    @Override
    @Transactional
    public BeerDto saveBeer(BeerDto beerDto) {
        Beer beer = beerMapper.beerDtoToBeer(beerDto);
        Beer savedBeer = beerRepository.save(beer);
        return beerMapper.beerToBeerDto(savedBeer);
    }

    @Override
    @Transactional
    public Optional<BeerDto> updateBeer(Integer id, BeerDto beerDto) {
        return beerRepository.findById(id)
                .map(existingBeer -> {
                    Beer beer = beerMapper.beerDtoToBeer(beerDto);
                    existingBeer.setBeerName(beer.getBeerName());
                    existingBeer.setBeerStyle(beer.getBeerStyle());
                    existingBeer.setUpc(beer.getUpc());
                    existingBeer.setPrice(beer.getPrice());
                    existingBeer.setQuantityOnHand(beer.getQuantityOnHand());
                    return beerRepository.save(existingBeer);
                })
                .map(beerMapper::beerToBeerDto);
    }

    @Override
    @Transactional
    public boolean deleteBeer(Integer id) {
        if (beerRepository.existsById(id)) {
            beerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
