package org.fmi.plovdiv.carmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.YearMonth;

@Data
@AllArgsConstructor
public class MonthlyRequestsReportDTO {

    private YearMonth yearMonth;

    private int requests;
}
