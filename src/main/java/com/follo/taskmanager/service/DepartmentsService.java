package com.follo.taskmanager.service;

import com.follo.taskmanager.domain.Departments;
import com.follo.taskmanager.repository.DepartmentsRepository;
import com.follo.taskmanager.service.dto.DepartmentsDTO;
import com.follo.taskmanager.service.mapper.DepartmentsMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Departments}.
 */
@Service
@Transactional
public class DepartmentsService {

    private final Logger log = LoggerFactory.getLogger(DepartmentsService.class);

    private final DepartmentsRepository departmentsRepository;

    private final DepartmentsMapper departmentsMapper;

    public DepartmentsService(DepartmentsRepository departmentsRepository, DepartmentsMapper departmentsMapper) {
        this.departmentsRepository = departmentsRepository;
        this.departmentsMapper = departmentsMapper;
    }

    /**
     * Save a departments.
     *
     * @param departmentsDTO the entity to save.
     * @return the persisted entity.
     */
    public DepartmentsDTO save(DepartmentsDTO departmentsDTO) {
        log.debug("Request to save Departments : {}", departmentsDTO);
        Departments departments = departmentsMapper.toEntity(departmentsDTO);
        departments = departmentsRepository.save(departments);
        return departmentsMapper.toDto(departments);
    }

    /**
     * Partially update a departments.
     *
     * @param departmentsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DepartmentsDTO> partialUpdate(DepartmentsDTO departmentsDTO) {
        log.debug("Request to partially update Departments : {}", departmentsDTO);

        return departmentsRepository
            .findById(departmentsDTO.getId())
            .map(
                existingDepartments -> {
                    departmentsMapper.partialUpdate(existingDepartments, departmentsDTO);

                    return existingDepartments;
                }
            )
            .map(departmentsRepository::save)
            .map(departmentsMapper::toDto);
    }

    /**
     * Get all the departments.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DepartmentsDTO> findAll() {
        log.debug("Request to get all Departments");
        return departmentsRepository.findAll().stream().map(departmentsMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one departments by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DepartmentsDTO> findOne(Long id) {
        log.debug("Request to get Departments : {}", id);
        return departmentsRepository.findById(id).map(departmentsMapper::toDto);
    }

    /**
     * Delete the departments by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Departments : {}", id);
        departmentsRepository.deleteById(id);
    }
}
