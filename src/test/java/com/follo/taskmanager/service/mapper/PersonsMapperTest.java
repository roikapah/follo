package com.follo.taskmanager.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PersonsMapperTest {

    private PersonsMapper personsMapper;

    @BeforeEach
    public void setUp() {
        personsMapper = new PersonsMapperImpl();
    }
}
