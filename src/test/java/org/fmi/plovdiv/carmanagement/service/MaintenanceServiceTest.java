// MaintenanceServiceTest.java
package org.fmi.plovdiv.carmanagement.service;

import jakarta.persistence.EntityNotFoundException;
import org.fmi.plovdiv.carmanagement.dto.CreateMaintenanceDTO;
import org.fmi.plovdiv.carmanagement.dto.MonthlyRequestsReportDTO;
import org.fmi.plovdiv.carmanagement.dto.UpdateMaintenanceDTO;
import org.fmi.plovdiv.carmanagement.model.Car;
import org.fmi.plovdiv.carmanagement.model.Garage;
import org.fmi.plovdiv.carmanagement.model.Maintenance;
import org.fmi.plovdiv.carmanagement.repository.MaintenanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MaintenanceServiceTest {

    @Mock
    private MaintenanceRepository maintenanceRepository;

    @Mock
    private CarService carService;

    @Mock
    private GarageService garageService;

    @InjectMocks
    private MaintenanceService maintenanceService;

    private Car testCar;
    private Garage testGarage;
    private Maintenance testMaintenance;
    private CreateMaintenanceDTO createMaintenanceDTO;
    private UpdateMaintenanceDTO updateMaintenanceDTO;

    @BeforeEach
    void setUp() {
        testCar = new Car();
        testCar.setId(1L);

        testGarage = new Garage();
        testGarage.setId(1L);
        testGarage.setCapacity(5);
        testGarage.setMaintenances(new ArrayList<>());

        testMaintenance = new Maintenance();
        testMaintenance.setId(1L);
        testMaintenance.setCar(testCar);
        testMaintenance.setGarage(testGarage);
        testMaintenance.setServiceType("Oil Change");
        testMaintenance.setScheduledDate(LocalDate.now());

        createMaintenanceDTO = new CreateMaintenanceDTO();
        createMaintenanceDTO.setCarId(1L);
        createMaintenanceDTO.setGarageId(1L);
        createMaintenanceDTO.setServiceType("Oil Change");
        createMaintenanceDTO.setScheduledDate(LocalDate.now());

        updateMaintenanceDTO = new UpdateMaintenanceDTO();
        updateMaintenanceDTO.setCarId(1L);
        updateMaintenanceDTO.setGarageId(1L);
        updateMaintenanceDTO.setServiceType("Brake Check");
        updateMaintenanceDTO.setScheduledDate(LocalDate.now());
    }

    @Test
    void getAllMaintenances_ShouldReturnMaintenanceList() {
        // Given
        List<Maintenance> expectedMaintenances = List.of(testMaintenance);
        when(maintenanceRepository.findAll(any(Specification.class))).thenReturn(expectedMaintenances);

        // When
        List<Maintenance> result = maintenanceService.getAllMaintenances(
                1L, 1L, LocalDate.now(), LocalDate.now()
        );

        // Then
        assertNotNull(result);
        assertEquals(expectedMaintenances.size(), result.size());
        verify(maintenanceRepository).findAll(any(Specification.class));
    }

    @Test
    void getMaintenanceById_WhenExists_ShouldReturnMaintenance() {
        // Given
        when(maintenanceRepository.findById(1L)).thenReturn(Optional.of(testMaintenance));

        // When
        Maintenance result = maintenanceService.getMaintenanceById(1L);

        // Then
        assertNotNull(result);
        assertEquals(testMaintenance.getId(), result.getId());
        verify(maintenanceRepository).findById(1L);
    }

    @Test
    void getMaintenanceById_WhenNotExists_ShouldThrowException() {
        // Given
        when(maintenanceRepository.findById(1L)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(EntityNotFoundException.class, () -> maintenanceService.getMaintenanceById(1L));
        verify(maintenanceRepository).findById(1L);
    }

    @Test
    void saveMaintenance_WhenValidData_ShouldSaveMaintenance() {
        // Given
        when(carService.getCarById(1L)).thenReturn(testCar);
        when(garageService.getGarageById(1L)).thenReturn(testGarage);
        when(maintenanceRepository.countByGarageIdAndScheduledDate(
                1L,
                createMaintenanceDTO.getScheduledDate()
        )).thenReturn(0L);
        when(maintenanceRepository.save(any(Maintenance.class))).thenReturn(testMaintenance);

        // When
        Maintenance result = maintenanceService.saveMaintenance(createMaintenanceDTO);

        // Then
        assertNotNull(result);
        assertEquals(testMaintenance.getServiceType(), result.getServiceType());
        verify(maintenanceRepository).save(any(Maintenance.class));
    }

    @Test
    void saveMaintenance_WhenGarageCapacityExceeded_ShouldThrowException() {
        // Given
        when(carService.getCarById(1L)).thenReturn(testCar);
        when(garageService.getGarageById(1L)).thenReturn(testGarage);
        when(maintenanceRepository.countByGarageIdAndScheduledDate(
                1L,
                createMaintenanceDTO.getScheduledDate()
        )).thenReturn(5L);

        // When/Then
        assertThrows(IllegalStateException.class, () -> maintenanceService.saveMaintenance(createMaintenanceDTO));
        verify(maintenanceRepository, never()).save(any(Maintenance.class));
    }

    @Test
    void updateMaintenance_WhenValidData_ShouldUpdateMaintenance() {
        // Given
        when(carService.getCarById(1L)).thenReturn(testCar);
        when(garageService.getGarageById(1L)).thenReturn(testGarage);
        when(maintenanceRepository.save(any(Maintenance.class))).thenReturn(testMaintenance);

        // When
        Maintenance result = maintenanceService.updateMaintenance(testMaintenance, updateMaintenanceDTO);

        // Then
        assertNotNull(result);
        assertEquals(updateMaintenanceDTO.getServiceType(), result.getServiceType());
        verify(maintenanceRepository).save(any(Maintenance.class));
    }

    @Test
    void deleteMaintenance_ShouldDeleteMaintenance() {
        // Given
        doNothing().when(maintenanceRepository).deleteById(1L);

        // When
        maintenanceService.deleteMaintenance(1L);

        // Then
        verify(maintenanceRepository).deleteById(1L);
    }

    @Test
    void getMonthlyRequestsReport_ShouldReturnReport() {
        // Given
        YearMonth startMonth = YearMonth.now().minusMonths(1);
        YearMonth endMonth = YearMonth.now();
        List<Maintenance> maintenances = new ArrayList<>();
        maintenances.add(testMaintenance);
        testGarage.setMaintenances(maintenances);
        when(garageService.getGarageById(1L)).thenReturn(testGarage);

        // When
        List<MonthlyRequestsReportDTO> result = maintenanceService.getMonthlyRequestsReport(1L, startMonth, endMonth);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        verify(garageService).getGarageById(1L);
    }
}
