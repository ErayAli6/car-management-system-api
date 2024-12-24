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

    public Garage getGarageById(Long id) {
        return garageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Garage not found with id: " + id));
    }

    public Garage saveGarage(Garage garage) {
        return garageRepository.save(garage);
    }

    public void deleteGarage(Long id) {
        garageRepository.deleteById(id);
    }

    public List<GarageDailyAvailabilityReportDTO> getDailyAvailabilityReport(Long garageId, LocalDate startDate, LocalDate endDate) {
        Garage garage = getGarageById(garageId);
        List<Maintenance> filteredMaintenances = filterMaintenancesInDateRange(garage.getMaintenances(), startDate, endDate);
        return generateDailyReports(garage, filteredMaintenances, startDate, endDate);
    }

    private List<Maintenance> filterMaintenancesInDateRange(List<Maintenance> maintenances, LocalDate startDate, LocalDate endDate) {
        return maintenances.stream()
                .filter(maintenance -> isMaintenanceInDateRange(maintenance, startDate, endDate))
                .toList();
    }

    private boolean isMaintenanceInDateRange(Maintenance maintenance, LocalDate startDate, LocalDate endDate) {
        return !maintenance.getScheduledDate().isBefore(startDate) &&
                !maintenance.getScheduledDate().isAfter(endDate);
    }

    private List<GarageDailyAvailabilityReportDTO> generateDailyReports(Garage garage, List<Maintenance> maintenances, LocalDate startDate, LocalDate endDate) {
        List<GarageDailyAvailabilityReportDTO> report = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            GarageDailyAvailabilityReportDTO dailyReport = createDailyReport(garage, maintenances, date);
            report.add(dailyReport);
        }
        return report;
    }

    private GarageDailyAvailabilityReportDTO createDailyReport(Garage garage, List<Maintenance> maintenances, LocalDate date) {
        int requests = countMaintenanceRequestsForDate(maintenances, date);
        int availableCapacity = calculateAvailableCapacity(garage.getCapacity(), requests);
        return new GarageDailyAvailabilityReportDTO(date, requests, availableCapacity);
    }

    private int countMaintenanceRequestsForDate(List<Maintenance> maintenances, LocalDate date) {
        return (int) maintenances.stream()
                .filter(maintenance -> maintenance.getScheduledDate().isEqual(date))
                .count();
    }

    private int calculateAvailableCapacity(int totalCapacity, int requests) {
        return totalCapacity - requests;
    }
}
