package com.follo.taskmanager.web.rest;

import com.follo.taskmanager.repository.WorkNotesRepository;
import com.follo.taskmanager.service.WorkNotesService;
import com.follo.taskmanager.service.dto.WorkNotesDTO;
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
 * REST controller for managing {@link com.follo.taskmanager.domain.WorkNotes}.
 */
@RestController
@RequestMapping("/api")
public class WorkNotesResource {

    private final Logger log = LoggerFactory.getLogger(WorkNotesResource.class);

    private static final String ENTITY_NAME = "workNotes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkNotesService workNotesService;

    private final WorkNotesRepository workNotesRepository;

    public WorkNotesResource(WorkNotesService workNotesService, WorkNotesRepository workNotesRepository) {
        this.workNotesService = workNotesService;
        this.workNotesRepository = workNotesRepository;
    }

    /**
     * {@code POST  /work-notes} : Create a new workNotes.
     *
     * @param workNotesDTO the workNotesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workNotesDTO, or with status {@code 400 (Bad Request)} if the workNotes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/work-notes")
    public ResponseEntity<WorkNotesDTO> createWorkNotes(@Valid @RequestBody WorkNotesDTO workNotesDTO) throws URISyntaxException {
        log.debug("REST request to save WorkNotes : {}", workNotesDTO);
        if (workNotesDTO.getId() != null) {
            throw new BadRequestAlertException("A new workNotes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkNotesDTO result = workNotesService.save(workNotesDTO);
        return ResponseEntity
            .created(new URI("/api/work-notes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /work-notes/:id} : Updates an existing workNotes.
     *
     * @param id the id of the workNotesDTO to save.
     * @param workNotesDTO the workNotesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workNotesDTO,
     * or with status {@code 400 (Bad Request)} if the workNotesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workNotesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/work-notes/{id}")
    public ResponseEntity<WorkNotesDTO> updateWorkNotes(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WorkNotesDTO workNotesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WorkNotes : {}, {}", id, workNotesDTO);
        if (workNotesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workNotesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workNotesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WorkNotesDTO result = workNotesService.save(workNotesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workNotesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /work-notes/:id} : Partial updates given fields of an existing workNotes, field will ignore if it is null
     *
     * @param id the id of the workNotesDTO to save.
     * @param workNotesDTO the workNotesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workNotesDTO,
     * or with status {@code 400 (Bad Request)} if the workNotesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the workNotesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the workNotesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/work-notes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<WorkNotesDTO> partialUpdateWorkNotes(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WorkNotesDTO workNotesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WorkNotes partially : {}, {}", id, workNotesDTO);
        if (workNotesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workNotesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workNotesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkNotesDTO> result = workNotesService.partialUpdate(workNotesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workNotesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /work-notes} : get all the workNotes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workNotes in body.
     */
    @GetMapping("/work-notes")
    public List<WorkNotesDTO> getAllWorkNotes() {
        log.debug("REST request to get all WorkNotes");
        return workNotesService.findAll();
    }

    /**
     * {@code GET  /work-notes/:id} : get the "id" workNotes.
     *
     * @param id the id of the workNotesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workNotesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/work-notes/{id}")
    public ResponseEntity<WorkNotesDTO> getWorkNotes(@PathVariable Long id) {
        log.debug("REST request to get WorkNotes : {}", id);
        Optional<WorkNotesDTO> workNotesDTO = workNotesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workNotesDTO);
    }

    /**
     * {@code DELETE  /work-notes/:id} : delete the "id" workNotes.
     *
     * @param id the id of the workNotesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/work-notes/{id}")
    public ResponseEntity<Void> deleteWorkNotes(@PathVariable Long id) {
        log.debug("REST request to delete WorkNotes : {}", id);
        workNotesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
