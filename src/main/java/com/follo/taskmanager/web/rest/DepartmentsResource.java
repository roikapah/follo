package com.follo.taskmanager.web.rest;

import com.follo.taskmanager.repository.DepartmentsRepository;
import com.follo.taskmanager.service.DepartmentsService;
import com.follo.taskmanager.service.dto.DepartmentsDTO;
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
 * REST controller for managing {@link com.follo.taskmanager.domain.Departments}.
 */
@RestController
@RequestMapping("/api")
public class DepartmentsResource {

    private final Logger log = LoggerFactory.getLogger(DepartmentsResource.class);

    private static final String ENTITY_NAME = "departments";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepartmentsService departmentsService;

    private final DepartmentsRepository departmentsRepository;

    public DepartmentsResource(DepartmentsService departmentsService, DepartmentsRepository departmentsRepository) {
        this.departmentsService = departmentsService;
        this.departmentsRepository = departmentsRepository;
    }

    /**
     * {@code POST  /departments} : Create a new departments.
     *
     * @param departmentsDTO the departmentsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new departmentsDTO, or with status {@code 400 (Bad Request)} if the departments has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/departments")
    public ResponseEntity<DepartmentsDTO> createDepartments(@Valid @RequestBody DepartmentsDTO departmentsDTO) throws URISyntaxException {
        log.debug("REST request to save Departments : {}", departmentsDTO);
        if (departmentsDTO.getId() != null) {
            throw new BadRequestAlertException("A new departments cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DepartmentsDTO result = departmentsService.save(departmentsDTO);
        return ResponseEntity
            .created(new URI("/api/departments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /departments/:id} : Updates an existing departments.
     *
     * @param id the id of the departmentsDTO to save.
     * @param departmentsDTO the departmentsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated departmentsDTO,
     * or with status {@code 400 (Bad Request)} if the departmentsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the departmentsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/departments/{id}")
    public ResponseEntity<DepartmentsDTO> updateDepartments(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DepartmentsDTO departmentsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Departments : {}, {}", id, departmentsDTO);
        if (departmentsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, departmentsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!departmentsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DepartmentsDTO result = departmentsService.save(departmentsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, departmentsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /departments/:id} : Partial updates given fields of an existing departments, field will ignore if it is null
     *
     * @param id the id of the departmentsDTO to save.
     * @param departmentsDTO the departmentsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated departmentsDTO,
     * or with status {@code 400 (Bad Request)} if the departmentsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the departmentsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the departmentsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/departments/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<DepartmentsDTO> partialUpdateDepartments(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DepartmentsDTO departmentsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Departments partially : {}, {}", id, departmentsDTO);
        if (departmentsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, departmentsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!departmentsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DepartmentsDTO> result = departmentsService.partialUpdate(departmentsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, departmentsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /departments} : get all the departments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of departments in body.
     */
    @GetMapping("/departments")
    public List<DepartmentsDTO> getAllDepartments() {
        log.debug("REST request to get all Departments");
        return departmentsService.findAll();
    }

    /**
     * {@code GET  /departments/:id} : get the "id" departments.
     *
     * @param id the id of the departmentsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the departmentsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/departments/{id}")
    public ResponseEntity<DepartmentsDTO> getDepartments(@PathVariable Long id) {
        log.debug("REST request to get Departments : {}", id);
        Optional<DepartmentsDTO> departmentsDTO = departmentsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(departmentsDTO);
    }

    /**
     * {@code DELETE  /departments/:id} : delete the "id" departments.
     *
     * @param id the id of the departmentsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/departments/{id}")
    public ResponseEntity<Void> deleteDepartments(@PathVariable Long id) {
        log.debug("REST request to delete Departments : {}", id);
        departmentsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
