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
        validateGarageCapacity(garage);
        return createAndSaveMaintenance(maintenanceDTO, car, garage);
    }

    public void deleteMaintenance(Long id) {
        maintenanceRepository.deleteById(id);
    }

    public List<MonthlyRequestsReportDTO> getMonthlyRequestsReport(Long garageId, YearMonth startMonth, YearMonth endMonth) {
        Garage garage = garageService.getGarageById(garageId);
        List<Maintenance> filteredMaintenances = filterMaintenancesByDateRange(garage.getMaintenances(), startMonth, endMonth);
        return generateMonthlyReports(filteredMaintenances, startMonth, endMonth);
    }

    private void validateGarageCapacity(Garage garage) {
        long currentMaintenanceCount = getCurrentMaintenanceCount(garage.getId());
        if (isGarageCapacityExceeded(garage, currentMaintenanceCount)) {
            throw new IllegalStateException("Garage capacity exceeded for garage id: " + garage.getId());
        }
    }

    private long getCurrentMaintenanceCount(Long garageId) {
        return maintenanceRepository.countByGarageId(garageId);
    }

    private boolean isGarageCapacityExceeded(Garage garage, long currentMaintenanceCount) {
        return currentMaintenanceCount >= garage.getCapacity();
    }

    private Maintenance createAndSaveMaintenance(CreateMaintenanceDTO dto, Car car, Garage garage) {
        Maintenance maintenance = createMaintenanceFromDTO(dto, car, garage);
        return maintenanceRepository.save(maintenance);
    }

    private Maintenance createMaintenanceFromDTO(CreateMaintenanceDTO dto, Car car, Garage garage) {
        Maintenance maintenance = new Maintenance();
        maintenance.setCar(car);
        maintenance.setGarage(garage);
        maintenance.setServiceType(dto.getServiceType());
        maintenance.setScheduledDate(dto.getScheduledDate());
        return maintenance;
    }

    private List<Maintenance> filterMaintenancesByDateRange(List<Maintenance> maintenances, YearMonth startMonth, YearMonth endMonth) {
        return maintenances.stream()
                .filter(maintenance -> isMaintenanceInMonthRange(maintenance, startMonth, endMonth))
                .toList();
    }

    private boolean isMaintenanceInMonthRange(Maintenance maintenance, YearMonth startMonth, YearMonth endMonth) {
        YearMonth maintenanceMonth = YearMonth.from(maintenance.getScheduledDate());
        return !maintenanceMonth.isBefore(startMonth) && !maintenanceMonth.isAfter(endMonth);
    }

    private List<MonthlyRequestsReportDTO> generateMonthlyReports(
            List<Maintenance> maintenances,
            YearMonth startMonth,
            YearMonth endMonth) {
        List<MonthlyRequestsReportDTO> report = new ArrayList<>();

        for (YearMonth month = startMonth; !month.isAfter(endMonth); month = month.plusMonths(1)) {
            MonthlyRequestsReportDTO monthlyReport = createMonthlyReport(maintenances, month);
            report.add(monthlyReport);
        }

        return report;
    }

    private MonthlyRequestsReportDTO createMonthlyReport(List<Maintenance> maintenances, YearMonth month) {
        int requests = countMaintenanceRequestsForMonth(maintenances, month);
        return new MonthlyRequestsReportDTO(month, requests);
    }

    private int countMaintenanceRequestsForMonth(List<Maintenance> maintenances, YearMonth month) {
        return (int) maintenances.stream()
                .filter(maintenance -> YearMonth.from(maintenance.getScheduledDate()).equals(month))
                .count();
    }
}
