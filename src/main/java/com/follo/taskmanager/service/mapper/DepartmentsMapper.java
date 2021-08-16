package com.follo.taskmanager.service.mapper;

import com.follo.taskmanager.domain.*;
import com.follo.taskmanager.service.dto.DepartmentsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Departments} and its DTO {@link DepartmentsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DepartmentsMapper extends EntityMapper<DepartmentsDTO, Departments> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    DepartmentsDTO toDtoName(Departments departments);
}
