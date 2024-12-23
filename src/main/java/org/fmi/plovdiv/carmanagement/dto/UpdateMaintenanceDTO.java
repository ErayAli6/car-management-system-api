package org.fmi.plovdiv.carmanagement.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateMaintenanceDTO {

    private Long carId;

    private String serviceType;

    private LocalDate scheduledDate;

    private Long garageId;
}
