package org.fmi.plovdiv.carmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.fmi.plovdiv.carmanagement.dto.CreateMaintenanceDTO;
import org.fmi.plovdiv.carmanagement.dto.MonthlyRequestsReportDTO;
import org.fmi.plovdiv.carmanagement.dto.ResponseMaintenanceDTO;
import org.fmi.plovdiv.carmanagement.dto.UpdateMaintenanceDTO;
import org.fmi.plovdiv.carmanagement.mapper.MaintenanceMapper;
import org.fmi.plovdiv.carmanagement.model.Maintenance;
import org.fmi.plovdiv.carmanagement.service.MaintenanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/maintenance")
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    private final MaintenanceMapper maintenanceMapper;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseMaintenanceDTO> getMaintenanceById(@PathVariable Long id) {
        Optional<Maintenance> maintenance = maintenanceService.getMaintenanceById(id);
        return maintenance.map(value -> ResponseEntity.ok(maintenanceMapper.toDto(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseMaintenanceDTO> updateMaintenance(@PathVariable Long id, @RequestBody UpdateMaintenanceDTO maintenanceDTO) {
        Optional<Maintenance> maintenanceById = maintenanceService.getMaintenanceById(id);
        if(maintenanceById.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Maintenance maintenance = maintenanceService.updateMaintenance(maintenanceById.get(), maintenanceDTO);
        return ResponseEntity.ok(maintenanceMapper.toDto(maintenance));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteMaintenance(@PathVariable Long id) {
        maintenanceService.deleteMaintenance(id);
        return ResponseEntity.ok(true);
    }

    @GetMapping
    public ResponseEntity<List<ResponseMaintenanceDTO>> getAllMaintenances(
            @RequestParam(required = false) Long carId,
            @RequestParam(required = false) Long garageId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        List<Maintenance> maintenances = maintenanceService.getAllMaintenances();
        return ResponseEntity.ok(maintenances.stream().map(maintenanceMapper::toDto).collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<ResponseMaintenanceDTO> createMaintenance(@RequestBody CreateMaintenanceDTO maintenanceDTO) {
        Maintenance createdMaintenance = maintenanceService.saveMaintenance(maintenanceDTO);
        return ResponseEntity.ok(maintenanceMapper.toDto(createdMaintenance));
    }

    @GetMapping("/monthlyRequestsReport")
    public ResponseEntity<List<MonthlyRequestsReportDTO>> monthlyRequestsReport(
            @RequestParam Long garageId,
            @RequestParam String startMonth,
            @RequestParam String endMonth) {
        // Implement the logic for generating the report
        return ResponseEntity.ok(List.of());
    }
}
