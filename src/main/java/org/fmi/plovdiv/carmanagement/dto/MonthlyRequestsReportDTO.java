package org.fmi.plovdiv.carmanagement.dto;

import lombok.Data;

import java.time.YearMonth;

@Data
public class MonthlyRequestsReportDTO {

    private YearMonth yearMonth;

    private int requests;
}
