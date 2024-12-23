package org.fmi.plovdiv.carmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class GarageDailyAvailabilityReportDTO {

    private LocalDate date;

    private int requests;

    private int availableCapacity;
}
