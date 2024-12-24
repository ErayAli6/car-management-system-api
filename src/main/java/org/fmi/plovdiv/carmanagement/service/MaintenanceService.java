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
        maintenance.setCar(carService.getCarById(maintenanceDTO.getCarId()));
        maintenance.setServiceType(maintenanceDTO.getServiceType());
        maintenance.setScheduledDate(maintenanceDTO.getScheduledDate());
        maintenance.setGarage(garageService.getGarageById(maintenanceDTO.getGarageId()));
        return maintenanceRepository.save(maintenance);
    }

    public Maintenance getMaintenanceById(Long id) {
        return maintenanceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Maintenance not found with id: " + id));
    }

    public Maintenance saveMaintenance(CreateMaintenanceDTO maintenanceDTO) {
        Car car = carService.getCarById(maintenanceDTO.getCarId());
        Garage garage = garageService.getGarageById(maintenanceDTO.getGarageId());
        long currentMaintenanceCount = maintenanceRepository.countByGarageId(garage.getId());
        if (currentMaintenanceCount >= garage.getCapacity()) {
            throw new IllegalStateException("Garage capacity exceeded for garage id: " + garage.getId());
        }
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
        Garage garage = garageService.getGarageById(garageId);
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
