package org.fmi.plovdiv.carmanagement.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateMaintenanceDTO {

    private Long carId;

    private Long garageId;

    private String serviceType;

    private LocalDate scheduledDate;
}
