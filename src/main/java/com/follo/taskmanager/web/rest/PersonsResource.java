package com.follo.taskmanager.web.rest;

import com.follo.taskmanager.repository.PersonsRepository;
import com.follo.taskmanager.service.PersonsService;
import com.follo.taskmanager.service.dto.PersonsDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.follo.taskmanager.domain.Persons}.
 */
@RestController
@RequestMapping("/api")
public class PersonsResource {

    private final Logger log = LoggerFactory.getLogger(PersonsResource.class);

    private static final String ENTITY_NAME = "persons";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PersonsService personsService;

    private final PersonsRepository personsRepository;

    public PersonsResource(PersonsService personsService, PersonsRepository personsRepository) {
        this.personsService = personsService;
        this.personsRepository = personsRepository;
    }

    /**
     * {@code POST  /persons} : Create a new persons.
     *
     * @param personsDTO the personsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new personsDTO, or with status {@code 400 (Bad Request)} if the persons has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/persons")
    public ResponseEntity<PersonsDTO> createPersons(@Valid @RequestBody PersonsDTO personsDTO) throws URISyntaxException {
        log.debug("REST request to save Persons : {}", personsDTO);
        if (personsDTO.getId() != null) {
            throw new BadRequestAlertException("A new persons cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PersonsDTO result = personsService.save(personsDTO);
        return ResponseEntity
            .created(new URI("/api/persons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /persons/:id} : Updates an existing persons.
     *
     * @param id the id of the personsDTO to save.
     * @param personsDTO the personsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personsDTO,
     * or with status {@code 400 (Bad Request)} if the personsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the personsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/persons/{id}")
    public ResponseEntity<PersonsDTO> updatePersons(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PersonsDTO personsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Persons : {}, {}", id, personsDTO);
        if (personsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PersonsDTO result = personsService.save(personsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /persons/:id} : Partial updates given fields of an existing persons, field will ignore if it is null
     *
     * @param id the id of the personsDTO to save.
     * @param personsDTO the personsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personsDTO,
     * or with status {@code 400 (Bad Request)} if the personsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the personsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the personsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/persons/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PersonsDTO> partialUpdatePersons(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PersonsDTO personsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Persons partially : {}, {}", id, personsDTO);
        if (personsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PersonsDTO> result = personsService.partialUpdate(personsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /persons} : get all the persons.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of persons in body.
     */
    @GetMapping("/persons")
    public ResponseEntity<List<PersonsDTO>> getAllPersons(Pageable pageable) {
        log.debug("REST request to get a page of Persons");
        Page<PersonsDTO> page = personsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /persons/:id} : get the "id" persons.
     *
     * @param id the id of the personsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the personsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/persons/{id}")
    public ResponseEntity<PersonsDTO> getPersons(@PathVariable Long id) {
        log.debug("REST request to get Persons : {}", id);
        Optional<PersonsDTO> personsDTO = personsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(personsDTO);
    }

    /**
     * {@code DELETE  /persons/:id} : delete the "id" persons.
     *
     * @param id the id of the personsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/persons/{id}")
    public ResponseEntity<Void> deletePersons(@PathVariable Long id) {
        log.debug("REST request to delete Persons : {}", id);
        personsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
