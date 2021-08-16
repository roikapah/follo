package com.follo.taskmanager.service;

import com.follo.taskmanager.domain.Persons;
import com.follo.taskmanager.repository.PersonsRepository;
import com.follo.taskmanager.service.dto.PersonsDTO;
import com.follo.taskmanager.service.mapper.PersonsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Persons}.
 */
@Service
@Transactional
public class PersonsService {

    private final Logger log = LoggerFactory.getLogger(PersonsService.class);

    private final PersonsRepository personsRepository;

    private final PersonsMapper personsMapper;

    public PersonsService(PersonsRepository personsRepository, PersonsMapper personsMapper) {
        this.personsRepository = personsRepository;
        this.personsMapper = personsMapper;
    }

    /**
     * Save a persons.
     *
     * @param personsDTO the entity to save.
     * @return the persisted entity.
     */
    public PersonsDTO save(PersonsDTO personsDTO) {
        log.debug("Request to save Persons : {}", personsDTO);
        Persons persons = personsMapper.toEntity(personsDTO);
        persons = personsRepository.save(persons);
        return personsMapper.toDto(persons);
    }

    /**
     * Partially update a persons.
     *
     * @param personsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PersonsDTO> partialUpdate(PersonsDTO personsDTO) {
        log.debug("Request to partially update Persons : {}", personsDTO);

        return personsRepository
            .findById(personsDTO.getId())
            .map(
                existingPersons -> {
                    personsMapper.partialUpdate(existingPersons, personsDTO);

                    return existingPersons;
                }
            )
            .map(personsRepository::save)
            .map(personsMapper::toDto);
    }

    /**
     * Get all the persons.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PersonsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Persons");
        return personsRepository.findAll(pageable).map(personsMapper::toDto);
    }

    /**
     * Get one persons by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PersonsDTO> findOne(Long id) {
        log.debug("Request to get Persons : {}", id);
        return personsRepository.findById(id).map(personsMapper::toDto);
    }

    /**
     * Delete the persons by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Persons : {}", id);
        personsRepository.deleteById(id);
    }
}
