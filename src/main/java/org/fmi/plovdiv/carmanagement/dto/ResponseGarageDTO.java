package org.fmi.plovdiv.carmanagement.dto;

import lombok.Data;

@Data
public class ResponseGarageDTO {

    private Long id;

    private String name;

    private String location;

    private String city;

    private int capacity;
}
