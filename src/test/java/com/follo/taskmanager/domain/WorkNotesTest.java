package com.follo.taskmanager.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.follo.taskmanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkNotesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkNotes.class);
        WorkNotes workNotes1 = new WorkNotes();
        workNotes1.setId(1L);
        WorkNotes workNotes2 = new WorkNotes();
        workNotes2.setId(workNotes1.getId());
        assertThat(workNotes1).isEqualTo(workNotes2);
        workNotes2.setId(2L);
        assertThat(workNotes1).isNotEqualTo(workNotes2);
        workNotes1.setId(null);
        assertThat(workNotes1).isNotEqualTo(workNotes2);
    }
}
