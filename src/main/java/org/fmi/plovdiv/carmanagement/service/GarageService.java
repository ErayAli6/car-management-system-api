package org.fmi.plovdiv.carmanagement.service;

import lombok.RequiredArgsConstructor;
import org.fmi.plovdiv.carmanagement.dto.UpdateGarageDTO;
import org.fmi.plovdiv.carmanagement.model.Garage;
import org.fmi.plovdiv.carmanagement.repository.GarageRepository;
import org.fmi.plovdiv.carmanagement.repository.GarageSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GarageService {

    private final GarageRepository garageRepository;

    public List<Garage> getAllGarages(String city) {
        Specification<Garage> spec = Specification.where(GarageSpecification.hasCity(city));
        return garageRepository.findAll(spec);
    }

    public Garage updateGarage(Garage garage, UpdateGarageDTO garageDTO) {
        garage.setName(garageDTO.getName());
        garage.setCity(garageDTO.getCity());
        garage.setCapacity(garageDTO.getCapacity());
        garage.setLocation(garageDTO.getLocation());
        return garageRepository.save(garage);
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
