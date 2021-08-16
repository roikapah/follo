package com.follo.taskmanager.service.mapper;

import com.follo.taskmanager.domain.*;
import com.follo.taskmanager.service.dto.PersonsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Persons} and its DTO {@link PersonsDTO}.
 */
@Mapper(componentModel = "spring", uses = { DepartmentsMapper.class })
public interface PersonsMapper extends EntityMapper<PersonsDTO, Persons> {
    @Mapping(target = "department", source = "department", qualifiedByName = "name")
    PersonsDTO toDto(Persons s);

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    PersonsDTO toDtoName(Persons persons);
}
