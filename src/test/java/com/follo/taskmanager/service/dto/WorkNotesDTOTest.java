package com.follo.taskmanager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.follo.taskmanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkNotesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkNotesDTO.class);
        WorkNotesDTO workNotesDTO1 = new WorkNotesDTO();
        workNotesDTO1.setId(1L);
        WorkNotesDTO workNotesDTO2 = new WorkNotesDTO();
        assertThat(workNotesDTO1).isNotEqualTo(workNotesDTO2);
        workNotesDTO2.setId(workNotesDTO1.getId());
        assertThat(workNotesDTO1).isEqualTo(workNotesDTO2);
        workNotesDTO2.setId(2L);
        assertThat(workNotesDTO1).isNotEqualTo(workNotesDTO2);
        workNotesDTO1.setId(null);
        assertThat(workNotesDTO1).isNotEqualTo(workNotesDTO2);
    }
}
