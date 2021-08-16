package com.follo.taskmanager.web.rest;

import com.follo.taskmanager.repository.AreaRepository;
import com.follo.taskmanager.service.AreaService;
import com.follo.taskmanager.service.dto.AreaDTO;
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
 * REST controller for managing {@link com.follo.taskmanager.domain.Area}.
 */
@RestController
@RequestMapping("/api")
public class AreaResource {

    private final Logger log = LoggerFactory.getLogger(AreaResource.class);

    private static final String ENTITY_NAME = "area";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AreaService areaService;

    private final AreaRepository areaRepository;

    public AreaResource(AreaService areaService, AreaRepository areaRepository) {
        this.areaService = areaService;
        this.areaRepository = areaRepository;
    }

    /**
     * {@code POST  /areas} : Create a new area.
     *
     * @param areaDTO the areaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new areaDTO, or with status {@code 400 (Bad Request)} if the area has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/areas")
    public ResponseEntity<AreaDTO> createArea(@Valid @RequestBody AreaDTO areaDTO) throws URISyntaxException {
        log.debug("REST request to save Area : {}", areaDTO);
        if (areaDTO.getId() != null) {
            throw new BadRequestAlertException("A new area cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AreaDTO result = areaService.save(areaDTO);
        return ResponseEntity
            .created(new URI("/api/areas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /areas/:id} : Updates an existing area.
     *
     * @param id the id of the areaDTO to save.
     * @param areaDTO the areaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated areaDTO,
     * or with status {@code 400 (Bad Request)} if the areaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the areaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/areas/{id}")
    public ResponseEntity<AreaDTO> updateArea(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AreaDTO areaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Area : {}, {}", id, areaDTO);
        if (areaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, areaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!areaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AreaDTO result = areaService.save(areaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, areaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /areas/:id} : Partial updates given fields of an existing area, field will ignore if it is null
     *
     * @param id the id of the areaDTO to save.
     * @param areaDTO the areaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated areaDTO,
     * or with status {@code 400 (Bad Request)} if the areaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the areaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the areaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/areas/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AreaDTO> partialUpdateArea(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AreaDTO areaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Area partially : {}, {}", id, areaDTO);
        if (areaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, areaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!areaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AreaDTO> result = areaService.partialUpdate(areaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, areaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /areas} : get all the areas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of areas in body.
     */
    @GetMapping("/areas")
    public List<AreaDTO> getAllAreas() {
        log.debug("REST request to get all Areas");
        return areaService.findAll();
    }

    /**
     * {@code GET  /areas/:id} : get the "id" area.
     *
     * @param id the id of the areaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the areaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/areas/{id}")
    public ResponseEntity<AreaDTO> getArea(@PathVariable Long id) {
        log.debug("REST request to get Area : {}", id);
        Optional<AreaDTO> areaDTO = areaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(areaDTO);
    }

    /**
     * {@code DELETE  /areas/:id} : delete the "id" area.
     *
     * @param id the id of the areaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/areas/{id}")
    public ResponseEntity<Void> deleteArea(@PathVariable Long id) {
        log.debug("REST request to delete Area : {}", id);
        areaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
