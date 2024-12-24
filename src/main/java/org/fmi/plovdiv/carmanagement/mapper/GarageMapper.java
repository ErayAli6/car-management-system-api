package org.fmi.plovdiv.carmanagement.mapper;

import org.fmi.plovdiv.carmanagement.dto.CreateGarageDTO;
import org.fmi.plovdiv.carmanagement.dto.ResponseGarageDTO;
import org.fmi.plovdiv.carmanagement.dto.UpdateGarageDTO;
import org.fmi.plovdiv.carmanagement.model.Garage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GarageMapper {

    Garage toEntity(CreateGarageDTO dto);

    ResponseGarageDTO toDto(Garage entity);
}
