package com.follo.taskmanager.web.rest;

import com.follo.taskmanager.repository.TaskTypeRepository;
import com.follo.taskmanager.service.TaskTypeService;
import com.follo.taskmanager.service.dto.TaskTypeDTO;
import com.follo.taskmanager.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.follo.taskmanager.domain.TaskType}.
 */
@RestController
@RequestMapping("/api")
public class TaskTypeResource {

    private final Logger log = LoggerFactory.getLogger(TaskTypeResource.class);

    private static final String ENTITY_NAME = "taskType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TaskTypeService taskTypeService;

    private final TaskTypeRepository taskTypeRepository;

    public TaskTypeResource(TaskTypeService taskTypeService, TaskTypeRepository taskTypeRepository) {
        this.taskTypeService = taskTypeService;
        this.taskTypeRepository = taskTypeRepository;
    }

    /**
     * {@code POST  /task-types} : Create a new taskType.
     *
     * @param taskTypeDTO the taskTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new taskTypeDTO, or with status {@code 400 (Bad Request)} if the taskType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/task-types")
    public ResponseEntity<TaskTypeDTO> createTaskType(@Valid @RequestBody TaskTypeDTO taskTypeDTO) throws URISyntaxException {
        log.debug("REST request to save TaskType : {}", taskTypeDTO);
        if (taskTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new taskType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TaskTypeDTO result = taskTypeService.save(taskTypeDTO);
        return ResponseEntity
            .created(new URI("/api/task-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /task-types/:id} : Updates an existing taskType.
     *
     * @param id the id of the taskTypeDTO to save.
     * @param taskTypeDTO the taskTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taskTypeDTO,
     * or with status {@code 400 (Bad Request)} if the taskTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the taskTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/task-types/{id}")
    public ResponseEntity<TaskTypeDTO> updateTaskType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TaskTypeDTO taskTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TaskType : {}, {}", id, taskTypeDTO);
        if (taskTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taskTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taskTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TaskTypeDTO result = taskTypeService.save(taskTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taskTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /task-types/:id} : Partial updates given fields of an existing taskType, field will ignore if it is null
     *
     * @param id the id of the taskTypeDTO to save.
     * @param taskTypeDTO the taskTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taskTypeDTO,
     * or with status {@code 400 (Bad Request)} if the taskTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the taskTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the taskTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/task-types/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TaskTypeDTO> partialUpdateTaskType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TaskTypeDTO taskTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TaskType partially : {}, {}", id, taskTypeDTO);
        if (taskTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taskTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taskTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TaskTypeDTO> result = taskTypeService.partialUpdate(taskTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taskTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /task-types} : get all the taskTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of taskTypes in body.
     */
    @GetMapping("/task-types")
    public List<TaskTypeDTO> getAllTaskTypes() {
        log.debug("REST request to get all TaskTypes");
        return taskTypeService.findAll();
    }

    /**
     * {@code GET  /task-types/:id} : get the "id" taskType.
     *
     * @param id the id of the taskTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the taskTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/task-types/{id}")
    public ResponseEntity<TaskTypeDTO> getTaskType(@PathVariable Long id) {
        log.debug("REST request to get TaskType : {}", id);
        Optional<TaskTypeDTO> taskTypeDTO = taskTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(taskTypeDTO);
    }

    /**
     * {@code DELETE  /task-types/:id} : delete the "id" taskType.
     *
     * @param id the id of the taskTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/task-types/{id}")
    public ResponseEntity<Void> deleteTaskType(@PathVariable Long id) {
        log.debug("REST request to delete TaskType : {}", id);
        taskTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
