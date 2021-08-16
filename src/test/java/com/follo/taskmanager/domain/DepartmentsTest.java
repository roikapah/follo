package com.follo.taskmanager.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.follo.taskmanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DepartmentsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Departments.class);
        Departments departments1 = new Departments();
        departments1.setId(1L);
        Departments departments2 = new Departments();
        departments2.setId(departments1.getId());
        assertThat(departments1).isEqualTo(departments2);
        departments2.setId(2L);
        assertThat(departments1).isNotEqualTo(departments2);
        departments1.setId(null);
        assertThat(departments1).isNotEqualTo(departments2);
    }
}
