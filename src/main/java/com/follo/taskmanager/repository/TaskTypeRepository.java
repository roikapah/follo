package com.follo.taskmanager.repository;

import com.follo.taskmanager.domain.TaskType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TaskType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaskTypeRepository extends JpaRepository<TaskType, Long> {}
