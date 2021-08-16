package com.follo.taskmanager.service.mapper;

import com.follo.taskmanager.domain.*;
import com.follo.taskmanager.service.dto.TaskDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Task} and its DTO {@link TaskDTO}.
 */
@Mapper(componentModel = "spring", uses = { PersonsMapper.class, DepartmentsMapper.class, AreaMapper.class, TaskTypeMapper.class })
public interface TaskMapper extends EntityMapper<TaskDTO, Task> {
    @Mapping(target = "assignTo", source = "assignTo", qualifiedByName = "name")
    @Mapping(target = "department", source = "department", qualifiedByName = "name")
    @Mapping(target = "area", source = "area", qualifiedByName = "name")
    @Mapping(target = "type", source = "type", qualifiedByName = "name")
    TaskDTO toDto(Task s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TaskDTO toDtoId(Task task);
}
