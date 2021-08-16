package com.follo.taskmanager.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.follo.taskmanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TaskTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaskType.class);
        TaskType taskType1 = new TaskType();
        taskType1.setId(1L);
        TaskType taskType2 = new TaskType();
        taskType2.setId(taskType1.getId());
        assertThat(taskType1).isEqualTo(taskType2);
        taskType2.setId(2L);
        assertThat(taskType1).isNotEqualTo(taskType2);
        taskType1.setId(null);
        assertThat(taskType1).isNotEqualTo(taskType2);
    }
}
