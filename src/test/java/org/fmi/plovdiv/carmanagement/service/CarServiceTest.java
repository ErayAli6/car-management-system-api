package org.fmi.plovdiv.carmanagement.service;

import jakarta.persistence.EntityNotFoundException;
import org.fmi.plovdiv.carmanagement.dto.UpdateCarDTO;
import org.fmi.plovdiv.carmanagement.model.Car;
import org.fmi.plovdiv.carmanagement.model.Garage;
import org.fmi.plovdiv.carmanagement.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private GarageService garageService;

    @InjectMocks
    private CarService carService;

    private Car testCar;
    private Garage testGarage;
    private UpdateCarDTO updateCarDTO;

    @BeforeEach
    void setUp() {
        testGarage = new Garage();
        testGarage.setId(1L);
        testGarage.setName("Test Garage");
        testGarage.setCity("Sofia");

        testCar = new Car();
        testCar.setId(1L);
        testCar.setMake("BMW");
        testCar.setModel("X5");
        testCar.setProductionYear(2020);
        testCar.setLicensePlate("CA1234XX");
        testCar.setGarages(new ArrayList<>(List.of(testGarage)));

        updateCarDTO = new UpdateCarDTO();
        updateCarDTO.setMake("Mercedes");
        updateCarDTO.setModel("C200");
        updateCarDTO.setProductionYear(2021);
        updateCarDTO.setLicensePlate("CA5678XX");
        updateCarDTO.setGarageIds(List.of(1L, 2L));
    }

    @Test
    void getAllCars_WhenFiltersProvided_ShouldReturnFilteredCars() {
        // Given
        String carMake = "BMW";
        Long garageId = 1L;
        Integer fromYear = 2019;
        Integer toYear = 2021;
        List<Car> expectedCars = List.of(testCar);
        when(carRepository.findAll(any(Specification.class))).thenReturn(expectedCars);

        // When
        List<Car> result = carService.getAllCars(carMake, garageId, fromYear, toYear);

        // Then
        assertThat(result)
                .isNotEmpty()
                .hasSize(1)
                .containsExactlyElementsOf(expectedCars);
        verify(carRepository).findAll(any(Specification.class));
    }

    @Test
    void getCarById_WhenValidId_ShouldReturnCar() {
        // Given
        when(carRepository.findById(1L)).thenReturn(Optional.of(testCar));

        // When
        Car result = carService.getCarById(1L);

        // Then
        assertThat(result)
                .isNotNull()
                .isEqualTo(testCar);
        verify(carRepository).findById(1L);
    }

    @Test
    void getCarById_WhenInvalidId_ShouldThrowException() {
        // Given
        when(carRepository.findById(1L)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(EntityNotFoundException.class, () -> carService.getCarById(1L));
        verify(carRepository).findById(1L);
    }

    @Test
    void updateCar_WhenValidDataProvided_ShouldUpdateCar() {
        // Given
        Garage garage1 = new Garage();
        garage1.setId(1L);
        Garage garage2 = new Garage();
        garage2.setId(2L);

        when(garageService.getGarageById(1L)).thenReturn(garage1);
        when(garageService.getGarageById(2L)).thenReturn(garage2);
        when(carRepository.save(any(Car.class))).thenReturn(testCar);

        // When
        Car result = carService.updateCar(testCar, updateCarDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getMake()).isEqualTo(updateCarDTO.getMake());
        assertThat(result.getModel()).isEqualTo(updateCarDTO.getModel());
        assertThat(result.getProductionYear()).isEqualTo(updateCarDTO.getProductionYear());
        assertThat(result.getLicensePlate()).isEqualTo(updateCarDTO.getLicensePlate());
        assertThat(result.getGarages()).hasSize(2);

        verify(garageService, times(2)).getGarageById(any());
        verify(carRepository).save(testCar);
    }

    @Test
    void saveCar_WhenValidCarAndGarages_ShouldSaveAndReturnCar() {
        // Given
        List<Long> garageIds = List.of(1L);
        Car newCar = new Car();
        newCar.setGarages(new ArrayList<>());

        when(garageService.getGarageById(1L)).thenReturn(testGarage);
        when(carRepository.save(any(Car.class))).thenReturn(newCar);

        // When
        Car result = carService.saveCar(newCar, garageIds);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getGarages()).hasSize(1);
        verify(garageService).getGarageById(1L);
        verify(carRepository).save(newCar);
    }

    @Test
    void deleteCar_WhenValidId_ShouldDeleteCar() {
        // Given
        doNothing().when(carRepository).deleteById(1L);

        // When
        carService.deleteCar(1L);

        // Then
        verify(carRepository).deleteById(1L);
    }

    @Test
    void updateCar_WhenInvalidGarageId_ShouldThrowException() {
        // Given
        when(garageService.getGarageById(any())).thenThrow(new EntityNotFoundException("Garage not found"));

        // When/Then
        assertThrows(EntityNotFoundException.class, () -> carService.updateCar(testCar, updateCarDTO));
        verify(carRepository, never()).save(any());
    }

    @Test
    void saveCar_WhenInvalidGarageId_ShouldThrowException() {
        // Given
        List<Long> garageIds = List.of(999L);
        Car newCar = new Car();
        newCar.setGarages(new ArrayList<>());

        when(garageService.getGarageById(999L)).thenThrow(new EntityNotFoundException("Garage not found"));

        // When/Then
        assertThrows(EntityNotFoundException.class, () -> carService.saveCar(newCar, garageIds));
        verify(carRepository, never()).save(any());
    }
}
