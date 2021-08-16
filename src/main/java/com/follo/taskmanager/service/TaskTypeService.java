package com.follo.taskmanager.service;

import com.follo.taskmanager.domain.TaskType;
import com.follo.taskmanager.repository.TaskTypeRepository;
import com.follo.taskmanager.service.dto.TaskTypeDTO;
import com.follo.taskmanager.service.mapper.TaskTypeMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TaskType}.
 */
@Service
@Transactional
public class TaskTypeService {

    private final Logger log = LoggerFactory.getLogger(TaskTypeService.class);

    private final TaskTypeRepository taskTypeRepository;

    private final TaskTypeMapper taskTypeMapper;

    public TaskTypeService(TaskTypeRepository taskTypeRepository, TaskTypeMapper taskTypeMapper) {
        this.taskTypeRepository = taskTypeRepository;
        this.taskTypeMapper = taskTypeMapper;
    }

    /**
     * Save a taskType.
     *
     * @param taskTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public TaskTypeDTO save(TaskTypeDTO taskTypeDTO) {
        log.debug("Request to save TaskType : {}", taskTypeDTO);
        TaskType taskType = taskTypeMapper.toEntity(taskTypeDTO);
        taskType = taskTypeRepository.save(taskType);
        return taskTypeMapper.toDto(taskType);
    }

    /**
     * Partially update a taskType.
     *
     * @param taskTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TaskTypeDTO> partialUpdate(TaskTypeDTO taskTypeDTO) {
        log.debug("Request to partially update TaskType : {}", taskTypeDTO);

        return taskTypeRepository
            .findById(taskTypeDTO.getId())
            .map(
                existingTaskType -> {
                    taskTypeMapper.partialUpdate(existingTaskType, taskTypeDTO);

                    return existingTaskType;
                }
            )
            .map(taskTypeRepository::save)
            .map(taskTypeMapper::toDto);
    }

    /**
     * Get all the taskTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TaskTypeDTO> findAll() {
        log.debug("Request to get all TaskTypes");
        return taskTypeRepository.findAll().stream().map(taskTypeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one taskType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TaskTypeDTO> findOne(Long id) {
        log.debug("Request to get TaskType : {}", id);
        return taskTypeRepository.findById(id).map(taskTypeMapper::toDto);
    }

    /**
     * Delete the taskType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TaskType : {}", id);
        taskTypeRepository.deleteById(id);
    }
}
