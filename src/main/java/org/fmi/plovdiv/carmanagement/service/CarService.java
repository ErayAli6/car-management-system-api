package org.fmi.plovdiv.carmanagement.service;

import lombok.RequiredArgsConstructor;
import org.fmi.plovdiv.carmanagement.dto.ResponseCarDTO;
import org.fmi.plovdiv.carmanagement.dto.UpdateCarDTO;
import org.fmi.plovdiv.carmanagement.model.Car;
import org.fmi.plovdiv.carmanagement.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CarService {

    private final CarRepository carRepository;

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id);
    }

    public Car updateCar(Car car, UpdateCarDTO carDTO) {
        car.setMake(carDTO.getMake());
        car.setModel(carDTO.getModel());
        car.setProductionYear(carDTO.getProductionYear());
        car.setLicensePlate(carDTO.getLicensePlate());
        car.setGarages(carDTO.getGarages());
        return carRepository.save(car);
    }

    public Car saveCar(Car car) {
        return carRepository.save(car);
    }

    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }
}
