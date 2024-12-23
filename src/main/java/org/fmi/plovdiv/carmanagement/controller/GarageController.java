package org.fmi.plovdiv.carmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.fmi.plovdiv.carmanagement.dto.CreateGarageDTO;
import org.fmi.plovdiv.carmanagement.dto.ResponseGarageDTO;
import org.fmi.plovdiv.carmanagement.dto.UpdateGarageDTO;
import org.fmi.plovdiv.carmanagement.mapper.GarageMapper;
import org.fmi.plovdiv.carmanagement.model.Garage;
import org.fmi.plovdiv.carmanagement.service.GarageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/garages")
public class GarageController {

    private final GarageService garageService;

    private final GarageMapper garageMapper;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseGarageDTO> getGarageById(@PathVariable Long id) {
        Optional<Garage> garage = garageService.getGarageById(id);
        return garage.map(value -> ResponseEntity.ok(garageMapper.toDto(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseGarageDTO> updateGarage(@PathVariable Long id, @RequestBody UpdateGarageDTO garageDTO) {
        Garage garage = garageMapper.toEntity(garageDTO);
        garage.setId(id);
        Garage updatedGarage = garageService.saveGarage(garage);
        return ResponseEntity.ok(garageMapper.toDto(updatedGarage));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteGarageById(@PathVariable Long id) {
        garageService.deleteGarage(id);
        return ResponseEntity.ok(true);
    }

    @GetMapping
    public ResponseEntity<List<ResponseGarageDTO>> getAllGarages(@RequestParam(required = false) String city) {
        List<Garage> garages = garageService.getAllGarages();
        return ResponseEntity.ok(garages.stream().map(garageMapper::toDto).collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<ResponseGarageDTO> createGarage(@RequestBody CreateGarageDTO garageDTO) {
        Garage garage = garageMapper.toEntity(garageDTO);
        Garage createdGarage = garageService.saveGarage(garage);
        return ResponseEntity.ok(garageMapper.toDto(createdGarage));
    }
}
