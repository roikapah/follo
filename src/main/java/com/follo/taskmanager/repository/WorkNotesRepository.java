package com.follo.taskmanager.repository;

import com.follo.taskmanager.domain.WorkNotes;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WorkNotes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkNotesRepository extends JpaRepository<WorkNotes, Long> {}
