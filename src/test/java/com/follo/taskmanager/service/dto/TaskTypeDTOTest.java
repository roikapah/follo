package com.follo.taskmanager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.follo.taskmanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TaskTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaskTypeDTO.class);
        TaskTypeDTO taskTypeDTO1 = new TaskTypeDTO();
        taskTypeDTO1.setId(1L);
        TaskTypeDTO taskTypeDTO2 = new TaskTypeDTO();
        assertThat(taskTypeDTO1).isNotEqualTo(taskTypeDTO2);
        taskTypeDTO2.setId(taskTypeDTO1.getId());
        assertThat(taskTypeDTO1).isEqualTo(taskTypeDTO2);
        taskTypeDTO2.setId(2L);
        assertThat(taskTypeDTO1).isNotEqualTo(taskTypeDTO2);
        taskTypeDTO1.setId(null);
        assertThat(taskTypeDTO1).isNotEqualTo(taskTypeDTO2);
    }
}
