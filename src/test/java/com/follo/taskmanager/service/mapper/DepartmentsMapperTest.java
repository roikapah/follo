package com.follo.taskmanager.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DepartmentsMapperTest {

    private DepartmentsMapper departmentsMapper;

    @BeforeEach
    public void setUp() {
        departmentsMapper = new DepartmentsMapperImpl();
    }
}
