package com.follo.taskmanager.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaskTypeMapperTest {

    private TaskTypeMapper taskTypeMapper;

    @BeforeEach
    public void setUp() {
        taskTypeMapper = new TaskTypeMapperImpl();
    }
}
