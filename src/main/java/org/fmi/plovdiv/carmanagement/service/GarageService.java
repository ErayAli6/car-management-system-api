package org.fmi.plovdiv.carmanagement.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.fmi.plovdiv.carmanagement.dto.GarageDailyAvailabilityReportDTO;
import org.fmi.plovdiv.carmanagement.dto.UpdateGarageDTO;
import org.fmi.plovdiv.carmanagement.model.Garage;
import org.fmi.plovdiv.carmanagement.model.Maintenance;
import org.fmi.plovdiv.carmanagement.repository.GarageRepository;
import org.fmi.plovdiv.carmanagement.repository.GarageSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
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

    public List<GarageDailyAvailabilityReportDTO> getDailyAvailabilityReport(Long garageId, LocalDate startDate, LocalDate endDate) {
        Optional<Garage> garageOptional = garageRepository.findById(garageId);
        if (garageOptional.isEmpty()) {
            throw new EntityNotFoundException("Garage not found with id: " + garageId);
        }

        Garage garage = garageOptional.get();
        List<Maintenance> maintenances = garage.getMaintenances()
                .stream()
                .filter(m -> !m.getScheduledDate().isBefore(startDate) && !m.getScheduledDate().isAfter(endDate))
                .toList();

        List<GarageDailyAvailabilityReportDTO> report = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            LocalDate finalDate = date;
            int requests = (int) maintenances.stream().filter(m -> m.getScheduledDate().isEqual(finalDate)).count();
            int availableCapacity = garage.getCapacity() - requests;
            GarageDailyAvailabilityReportDTO reportDTO = new GarageDailyAvailabilityReportDTO(date, requests, availableCapacity);
            report.add(reportDTO);
        }

        return report;
    }
}
