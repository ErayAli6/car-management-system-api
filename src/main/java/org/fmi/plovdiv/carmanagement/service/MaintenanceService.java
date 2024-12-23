package org.fmi.plovdiv.carmanagement.service;

import lombok.RequiredArgsConstructor;
import org.fmi.plovdiv.carmanagement.dto.UpdateMaintenanceDTO;
import org.fmi.plovdiv.carmanagement.model.Maintenance;
import org.fmi.plovdiv.carmanagement.repository.MaintenanceRepository;
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

    public List<Maintenance> getAllMaintenances() {
        return maintenanceRepository.findAll();
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

    public Maintenance saveMaintenance(Maintenance maintenance) {
        return maintenanceRepository.save(maintenance);
    }

    public void deleteMaintenance(Long id) {
        maintenanceRepository.deleteById(id);
    }
}
