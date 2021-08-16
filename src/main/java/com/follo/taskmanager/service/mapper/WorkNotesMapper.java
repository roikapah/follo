package com.follo.taskmanager.service.mapper;

import com.follo.taskmanager.domain.*;
import com.follo.taskmanager.service.dto.WorkNotesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkNotes} and its DTO {@link WorkNotesDTO}.
 */
@Mapper(componentModel = "spring", uses = { PersonsMapper.class, TaskMapper.class })
public interface WorkNotesMapper extends EntityMapper<WorkNotesDTO, WorkNotes> {
    @Mapping(target = "createBy", source = "createBy", qualifiedByName = "name")
    @Mapping(target = "task", source = "task", qualifiedByName = "id")
    WorkNotesDTO toDto(WorkNotes s);
}
