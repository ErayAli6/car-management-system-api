package org.fmi.plovdiv.carmanagement.dto;

import lombok.Data;

@Data
public class YearMonthDTO {

    private int year;

    private MonthEnum month;

    private boolean leapYear;

    private int monthValue;
}
