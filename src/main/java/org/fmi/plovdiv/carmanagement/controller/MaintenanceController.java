package org.fmi.plovdiv.carmanagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.fmi.plovdiv.carmanagement.dto.CreateMaintenanceDTO;
import org.fmi.plovdiv.carmanagement.dto.MonthlyRequestsReportDTO;
import org.fmi.plovdiv.carmanagement.dto.ResponseMaintenanceDTO;
import org.fmi.plovdiv.carmanagement.dto.UpdateMaintenanceDTO;
import org.fmi.plovdiv.carmanagement.mapper.MaintenanceMapper;
import org.fmi.plovdiv.carmanagement.model.Maintenance;
import org.fmi.plovdiv.carmanagement.service.MaintenanceService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/maintenance")
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    private final MaintenanceMapper maintenanceMapper;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseMaintenanceDTO> getMaintenanceById(@PathVariable Long id) {
        Maintenance maintenance = maintenanceService.getMaintenanceById(id);
        return ResponseEntity.ok(maintenanceMapper.toDto(maintenance));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseMaintenanceDTO> updateMaintenance(@PathVariable Long id, @Valid @RequestBody UpdateMaintenanceDTO maintenanceDTO) {
        Maintenance maintenanceById = maintenanceService.getMaintenanceById(id);
        Maintenance maintenance = maintenanceService.updateMaintenance(maintenanceById, maintenanceDTO);
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
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Maintenance> maintenances = maintenanceService.getAllMaintenances(carId, garageId, startDate, endDate);
        return ResponseEntity.ok(maintenances.stream().map(maintenanceMapper::toDto).collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<ResponseMaintenanceDTO> createMaintenance(@Valid @RequestBody CreateMaintenanceDTO maintenanceDTO) {
        Maintenance createdMaintenance = maintenanceService.saveMaintenance(maintenanceDTO);
        return ResponseEntity.ok(maintenanceMapper.toDto(createdMaintenance));
    }

    @GetMapping("/monthlyRequestsReport")
    public ResponseEntity<List<MonthlyRequestsReportDTO>> monthlyRequestsReport(
            @RequestParam Long garageId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth startMonth,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth endMonth) {
        List<MonthlyRequestsReportDTO> report = maintenanceService.getMonthlyRequestsReport(garageId, startMonth, endMonth);
        return ResponseEntity.ok(report);
    }
}
