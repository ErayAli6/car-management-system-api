package org.fmi.plovdiv.carmanagement.service;

import lombok.RequiredArgsConstructor;
import org.fmi.plovdiv.carmanagement.model.Garage;
import org.fmi.plovdiv.carmanagement.repository.GarageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GarageService {

    private final GarageRepository garageRepository;

    public List<Garage> getAllGarages() {
        return garageRepository.findAll();
    }

    public Optional<Garage> getGarageById(Long id) {
        return garageRepository.findById(id);
    }

    public Garage saveGarage(Garage garage) {
        return garageRepository.save(garage);
    }

    public void deleteGarage(Long id) {
        garageRepository.deleteById(id);
    }
}
