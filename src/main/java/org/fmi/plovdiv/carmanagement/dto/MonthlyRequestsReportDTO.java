package org.fmi.plovdiv.carmanagement.dto;

import lombok.Data;

@Data
public class MonthlyRequestsReportDTO {

    private YearMonthDTO yearMonth;

    private int requests;
}
