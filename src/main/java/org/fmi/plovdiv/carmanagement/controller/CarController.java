package org.fmi.plovdiv.carmanagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.fmi.plovdiv.carmanagement.dto.CreateCarDTO;
import org.fmi.plovdiv.carmanagement.dto.ResponseCarDTO;
import org.fmi.plovdiv.carmanagement.dto.UpdateCarDTO;
import org.fmi.plovdiv.carmanagement.mapper.CarMapper;
import org.fmi.plovdiv.carmanagement.model.Car;
import org.fmi.plovdiv.carmanagement.service.CarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;

    private final CarMapper carMapper;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseCarDTO> getCarById(@PathVariable Long id) {
        Car car = carService.getCarById(id);
        return ResponseEntity.ok(carMapper.toDto(car));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseCarDTO> updateCar(@PathVariable Long id, @Valid @RequestBody UpdateCarDTO carDTO) {
        Car carById = carService.getCarById(id);
        ResponseCarDTO responseCarDTO = carMapper.toDto(carService.updateCar(carById, carDTO));
        return ResponseEntity.ok(responseCarDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteCarById(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.ok(true);
    }

    @GetMapping
    public ResponseEntity<List<ResponseCarDTO>> getAllCars(
            @RequestParam(required = false) String carMake,
            @RequestParam(required = false) Long garageId,
            @RequestParam(required = false) Integer fromYear,
            @RequestParam(required = false) Integer toYear) {
        List<Car> cars = carService.getAllCars(carMake, garageId, fromYear, toYear);
        return ResponseEntity.ok(cars.stream().map(carMapper::toDto).collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<ResponseCarDTO> createCar(@Valid @RequestBody CreateCarDTO carDTO) {
        Car car = carMapper.toEntity(carDTO);
        Car createdCar = carService.saveCar(car, carDTO.getGarageIds());
        return ResponseEntity.ok(carMapper.toDto(createdCar));
    }
}
