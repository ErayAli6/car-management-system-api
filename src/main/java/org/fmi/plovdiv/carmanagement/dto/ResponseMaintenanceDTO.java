package org.fmi.plovdiv.carmanagement.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ResponseMaintenanceDTO {

    private Long id;

    private Long carId;

    private String carName;

    private String serviceType;

    private LocalDate scheduledDate;

    private Long garageId;

    private String garageName;
}
