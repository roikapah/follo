package com.follo.taskmanager.service.mapper;

import com.follo.taskmanager.domain.*;
import com.follo.taskmanager.service.dto.TaskTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TaskType} and its DTO {@link TaskTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TaskTypeMapper extends EntityMapper<TaskTypeDTO, TaskType> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    TaskTypeDTO toDtoName(TaskType taskType);
}
