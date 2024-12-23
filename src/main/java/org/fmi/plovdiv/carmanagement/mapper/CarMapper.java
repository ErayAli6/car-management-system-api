package org.fmi.plovdiv.carmanagement.mapper;

import org.fmi.plovdiv.carmanagement.dto.CreateCarDTO;
import org.fmi.plovdiv.carmanagement.dto.ResponseCarDTO;
import org.fmi.plovdiv.carmanagement.dto.UpdateCarDTO;
import org.fmi.plovdiv.carmanagement.model.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CarMapper {

    Car toEntity(CreateCarDTO dto);

    Car toEntity(UpdateCarDTO dto);

    @Mapping(target = "garageIds", source = "garages.id")
    ResponseCarDTO toDto(Car entity);
}
