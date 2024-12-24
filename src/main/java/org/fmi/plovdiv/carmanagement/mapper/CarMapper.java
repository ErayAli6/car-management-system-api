package org.fmi.plovdiv.carmanagement.mapper;

import org.fmi.plovdiv.carmanagement.dto.CreateCarDTO;
import org.fmi.plovdiv.carmanagement.dto.ResponseCarDTO;
import org.fmi.plovdiv.carmanagement.dto.ResponseGarageDTO;
import org.fmi.plovdiv.carmanagement.model.Car;
import org.fmi.plovdiv.carmanagement.model.Garage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CarMapper {

    Car toEntity(CreateCarDTO dto);

    ResponseCarDTO toDto(Car car);

    ResponseGarageDTO toGarageDto(Garage garage);
}
