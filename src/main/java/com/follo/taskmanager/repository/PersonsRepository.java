package com.follo.taskmanager.repository;

import com.follo.taskmanager.domain.Persons;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Persons entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonsRepository extends JpaRepository<Persons, Long> {}
