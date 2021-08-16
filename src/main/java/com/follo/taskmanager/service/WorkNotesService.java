package com.follo.taskmanager.service;

import com.follo.taskmanager.domain.WorkNotes;
import com.follo.taskmanager.repository.WorkNotesRepository;
import com.follo.taskmanager.service.dto.WorkNotesDTO;
import com.follo.taskmanager.service.mapper.WorkNotesMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WorkNotes}.
 */
@Service
@Transactional
public class WorkNotesService {

    private final Logger log = LoggerFactory.getLogger(WorkNotesService.class);

    private final WorkNotesRepository workNotesRepository;

    private final WorkNotesMapper workNotesMapper;

    public WorkNotesService(WorkNotesRepository workNotesRepository, WorkNotesMapper workNotesMapper) {
        this.workNotesRepository = workNotesRepository;
        this.workNotesMapper = workNotesMapper;
    }

    /**
     * Save a workNotes.
     *
     * @param workNotesDTO the entity to save.
     * @return the persisted entity.
     */
    public WorkNotesDTO save(WorkNotesDTO workNotesDTO) {
        log.debug("Request to save WorkNotes : {}", workNotesDTO);
        WorkNotes workNotes = workNotesMapper.toEntity(workNotesDTO);
        workNotes = workNotesRepository.save(workNotes);
        return workNotesMapper.toDto(workNotes);
    }

    /**
     * Partially update a workNotes.
     *
     * @param workNotesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WorkNotesDTO> partialUpdate(WorkNotesDTO workNotesDTO) {
        log.debug("Request to partially update WorkNotes : {}", workNotesDTO);

        return workNotesRepository
            .findById(workNotesDTO.getId())
            .map(
                existingWorkNotes -> {
                    workNotesMapper.partialUpdate(existingWorkNotes, workNotesDTO);

                    return existingWorkNotes;
                }
            )
            .map(workNotesRepository::save)
            .map(workNotesMapper::toDto);
    }

    /**
     * Get all the workNotes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<WorkNotesDTO> findAll() {
        log.debug("Request to get all WorkNotes");
        return workNotesRepository.findAll().stream().map(workNotesMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one workNotes by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WorkNotesDTO> findOne(Long id) {
        log.debug("Request to get WorkNotes : {}", id);
        return workNotesRepository.findById(id).map(workNotesMapper::toDto);
    }

    /**
     * Delete the workNotes by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WorkNotes : {}", id);
        workNotesRepository.deleteById(id);
    }
}
