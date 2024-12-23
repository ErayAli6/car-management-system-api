package org.fmi.plovdiv.carmanagement.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class GarageDailyAvailabilityReportDTO {

    private LocalDate date;

    private int requests;

    private int availableCapacity;
}
