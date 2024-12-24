package org.fmi.plovdiv.carmanagement.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.fmi.plovdiv.carmanagement.dto.UpdateCarDTO;
import org.fmi.plovdiv.carmanagement.model.Car;
import org.fmi.plovdiv.carmanagement.model.Garage;
import org.fmi.plovdiv.carmanagement.repository.CarRepository;
import org.fmi.plovdiv.carmanagement.repository.CarSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CarService {

    private final CarRepository carRepository;

    private final GarageService garageService;

    public List<Car> getAllCars(String carMake, Long garageId, Integer fromYear, Integer toYear) {
        Specification<Car> spec = Specification.where(CarSpecification.hasMake(carMake))
                .and(CarSpecification.belongsToGarage(garageId))
                .and(CarSpecification.productionYearBetween(fromYear, toYear));
        return carRepository.findAll(spec);
    }

    public Car getCarById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Car not found with id: " + id));
    }

    public Car updateCar(Car car, UpdateCarDTO carDTO) {
        car.setMake(carDTO.getMake());
        car.setModel(carDTO.getModel());
        car.setProductionYear(carDTO.getProductionYear());
        car.setLicensePlate(carDTO.getLicensePlate());
        List<Garage> newGarages = new ArrayList<>();
        carDTO.getGarageIds().forEach(garageId -> newGarages.add(garageService.getGarageById(garageId)));
        car.setGarages(newGarages);
        return carRepository.save(car);
    }

    public Car saveCar(Car car, List<Long> garageIds) {
        garageIds.forEach(garageId -> car.getGarages().add(garageService.getGarageById(garageId)));
        return carRepository.save(car);
    }

    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }
}
