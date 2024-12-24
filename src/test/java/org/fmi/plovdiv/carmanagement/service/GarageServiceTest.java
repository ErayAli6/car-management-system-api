package org.fmi.plovdiv.carmanagement.service;

import jakarta.persistence.EntityNotFoundException;
import org.fmi.plovdiv.carmanagement.dto.GarageDailyAvailabilityReportDTO;
import org.fmi.plovdiv.carmanagement.dto.UpdateGarageDTO;
import org.fmi.plovdiv.carmanagement.model.Garage;
import org.fmi.plovdiv.carmanagement.model.Maintenance;
import org.fmi.plovdiv.carmanagement.repository.GarageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GarageServiceTest {

    @Mock
    private GarageRepository garageRepository;

    @InjectMocks
    private GarageService garageService;

    private Garage testGarage;
    private UpdateGarageDTO updateGarageDTO;

    @BeforeEach
    void setUp() {
        testGarage = new Garage();
        testGarage.setId(1L);
        testGarage.setName("Test Garage");
        testGarage.setCity("Sofia");
        testGarage.setCapacity(5);
        testGarage.setLocation("Test Location");

        updateGarageDTO = new UpdateGarageDTO();
        updateGarageDTO.setName("Updated Garage");
        updateGarageDTO.setCity("Plovdiv");
        updateGarageDTO.setCapacity(10);
        updateGarageDTO.setLocation("Updated Location");

        List<Maintenance> maintenances = new ArrayList<>();
        Maintenance maintenance = new Maintenance();
        maintenance.setScheduledDate(LocalDate.now());
        maintenances.add(maintenance);
        testGarage.setMaintenances(maintenances);
    }

    @Test
    void getAllGarages_WhenCityProvided_ShouldReturnFilteredGarages() {
        // Given
        String city = "Sofia";
        List<Garage> expectedGarages = List.of(testGarage);
        when(garageRepository.findAll(any(Specification.class))).thenReturn(expectedGarages);

        // When
        List<Garage> result = garageService.getAllGarages(city);

        // Then
        assertThat(result).isNotEmpty()
                .hasSize(1)
                .containsExactlyElementsOf(expectedGarages);
        verify(garageRepository).findAll(any(Specification.class));
    }

    @Test
    void updateGarage_WhenValidDataProvided_ShouldUpdateGarage() {
        // Given
        when(garageRepository.save(any(Garage.class))).thenReturn(testGarage);

        // When
        Garage result = garageService.updateGarage(testGarage, updateGarageDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(updateGarageDTO.getName());
        assertThat(result.getCity()).isEqualTo(updateGarageDTO.getCity());
        assertThat(result.getCapacity()).isEqualTo(updateGarageDTO.getCapacity());
        assertThat(result.getLocation()).isEqualTo(updateGarageDTO.getLocation());
        verify(garageRepository).save(testGarage);
    }

    @Test
    void getGarageById_WhenValidId_ShouldReturnGarage() {
        // Given
        when(garageRepository.findById(1L)).thenReturn(Optional.of(testGarage));

        // When
        Garage result = garageService.getGarageById(1L);

        // Then
        assertThat(result).isNotNull()
                .isEqualTo(testGarage);
        verify(garageRepository).findById(1L);
    }

    @Test
    void getGarageById_WhenInvalidId_ShouldThrowException() {
        // Given
        when(garageRepository.findById(1L)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(EntityNotFoundException.class, () -> garageService.getGarageById(1L));
        verify(garageRepository).findById(1L);
    }

    @Test
    void saveGarage_WhenValidGarage_ShouldSaveAndReturnGarage() {
        // Given
        when(garageRepository.save(any(Garage.class))).thenReturn(testGarage);

        // When
        Garage result = garageService.saveGarage(testGarage);

        // Then
        assertThat(result).isNotNull()
                .isEqualTo(testGarage);
        verify(garageRepository).save(testGarage);
    }

    @Test
    void deleteGarage_WhenValidId_ShouldDeleteGarage() {
        // Given
        doNothing().when(garageRepository).deleteById(1L);

        // When
        garageService.deleteGarage(1L);

        // Then
        verify(garageRepository).deleteById(1L);
    }

    @Test
    void getDailyAvailabilityReport_WhenValidDateRange_ShouldReturnReport() {
        // Given
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(2);
        when(garageRepository.findById(1L)).thenReturn(Optional.of(testGarage));

        // When
        List<GarageDailyAvailabilityReportDTO> result =
                garageService.getDailyAvailabilityReport(1L, startDate, endDate);

        // Then
        assertThat(result).isNotNull()
                .hasSize(3);

        GarageDailyAvailabilityReportDTO firstDay = result.get(0);
        assertThat(firstDay.getDate()).isEqualTo(startDate);
        assertThat(firstDay.getRequests()).isEqualTo(1);
        assertThat(firstDay.getAvailableCapacity()).isEqualTo(testGarage.getCapacity() - 1);

        verify(garageRepository).findById(1L);
    }

    @Test
    void getDailyAvailabilityReport_WhenGarageNotFound_ShouldThrowException() {
        // Given
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(2);
        when(garageRepository.findById(1L)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(EntityNotFoundException.class,
                () -> garageService.getDailyAvailabilityReport(1L, startDate, endDate));
        verify(garageRepository).findById(1L);
    }
}
