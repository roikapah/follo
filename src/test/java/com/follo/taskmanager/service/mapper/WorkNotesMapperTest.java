package com.follo.taskmanager.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WorkNotesMapperTest {

    private WorkNotesMapper workNotesMapper;

    @BeforeEach
    public void setUp() {
        workNotesMapper = new WorkNotesMapperImpl();
    }
}
