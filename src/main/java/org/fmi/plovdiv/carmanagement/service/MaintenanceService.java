package org.fmi.plovdiv.carmanagement.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.fmi.plovdiv.carmanagement.dto.CreateMaintenanceDTO;
import org.fmi.plovdiv.carmanagement.dto.UpdateMaintenanceDTO;
import org.fmi.plovdiv.carmanagement.model.Car;
import org.fmi.plovdiv.carmanagement.model.Garage;
import org.fmi.plovdiv.carmanagement.model.Maintenance;
import org.fmi.plovdiv.carmanagement.repository.MaintenanceRepository;
import org.fmi.plovdiv.carmanagement.repository.MaintenanceSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;

    private final CarService carService;

    private final GarageService garageService;

    public List<Maintenance> getAllMaintenances(Long carId, Long garageId, LocalDate startDate, LocalDate endDate) {
        Specification<Maintenance> spec = Specification.where(MaintenanceSpecification.hasCarId(carId))
                .and(MaintenanceSpecification.hasGarageId(garageId))
                .and(MaintenanceSpecification.scheduledBetween(startDate, endDate));

        return maintenanceRepository.findAll(spec);
    }

    public Maintenance updateMaintenance(Maintenance maintenance, UpdateMaintenanceDTO maintenanceDTO) {
        maintenance.setCar(carService.getCarById(maintenanceDTO.getCarId()).get());
        maintenance.setServiceType(maintenanceDTO.getServiceType());
        maintenance.setScheduledDate(maintenanceDTO.getScheduledDate());
        maintenance.setGarage(garageService.getGarageById(maintenanceDTO.getGarageId()).get());
        return maintenanceRepository.save(maintenance);
    }
    public Optional<Maintenance> getMaintenanceById(Long id) {
        return maintenanceRepository.findById(id);
    }

    public Maintenance saveMaintenance(CreateMaintenanceDTO maintenanceDTO) {
        Car car = carService.getCarById(maintenanceDTO.getCarId())
                .orElseThrow(() -> new EntityNotFoundException("Car not found with id: " + maintenanceDTO.getCarId()));
        Garage garage = garageService.getGarageById(maintenanceDTO.getGarageId())
                .orElseThrow(() -> new EntityNotFoundException("Garage not found with id: " + maintenanceDTO.getGarageId()));
        Maintenance maintenance = new Maintenance();
        maintenance.setCar(car);
        maintenance.setGarage(garage);
        maintenance.setServiceType(maintenanceDTO.getServiceType());
        maintenance.setScheduledDate(maintenanceDTO.getScheduledDate());
        return maintenanceRepository.save(maintenance);
    }

    public void deleteMaintenance(Long id) {
        maintenanceRepository.deleteById(id);
    }
}
