package org.fmi.plovdiv.carmanagement.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.fmi.plovdiv.carmanagement.dto.CreateMaintenanceDTO;
import org.fmi.plovdiv.carmanagement.dto.MonthlyRequestsReportDTO;
import org.fmi.plovdiv.carmanagement.dto.UpdateMaintenanceDTO;
import org.fmi.plovdiv.carmanagement.model.Car;
import org.fmi.plovdiv.carmanagement.model.Garage;
import org.fmi.plovdiv.carmanagement.model.Maintenance;
import org.fmi.plovdiv.carmanagement.repository.MaintenanceRepository;
import org.fmi.plovdiv.carmanagement.repository.MaintenanceSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<MonthlyRequestsReportDTO> getMonthlyRequestsReport(Long garageId, YearMonth startMonth, YearMonth endMonth) {
        Optional<Garage> garageOptional = garageService.getGarageById(garageId);
        if (garageOptional.isEmpty()) {
            throw new EntityNotFoundException("Garage not found with id: " + garageId);
        }

        Garage garage = garageOptional.get();
        List<Maintenance> maintenances = garage.getMaintenances()
                .stream()
                .filter(m -> {
                    YearMonth maintenanceMonth = YearMonth.from(m.getScheduledDate());
                    return !maintenanceMonth.isBefore(startMonth) && !maintenanceMonth.isAfter(endMonth);
                })
                .toList();

        List<MonthlyRequestsReportDTO> report = new ArrayList<>();
        for (YearMonth month = startMonth; !month.isAfter(endMonth); month = month.plusMonths(1)) {
            YearMonth finalMonth = month;
            int requests = (int) maintenances.stream().filter(m -> YearMonth.from(m.getScheduledDate()).equals(finalMonth)).count();
            MonthlyRequestsReportDTO reportDTO = new MonthlyRequestsReportDTO(month, requests);
            report.add(reportDTO);
        }

        return report;
    }
}
