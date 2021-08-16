package com.follo.taskmanager.web.rest;

import static com.follo.taskmanager.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.follo.taskmanager.IntegrationTest;
import com.follo.taskmanager.domain.WorkNotes;
import com.follo.taskmanager.repository.WorkNotesRepository;
import com.follo.taskmanager.service.dto.WorkNotesDTO;
import com.follo.taskmanager.service.mapper.WorkNotesMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link WorkNotesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WorkNotesResourceIT {

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATE_ON = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATE_ON = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/work-notes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkNotesRepository workNotesRepository;

    @Autowired
    private WorkNotesMapper workNotesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkNotesMockMvc;

    private WorkNotes workNotes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkNotes createEntity(EntityManager em) {
        WorkNotes workNotes = new WorkNotes().text(DEFAULT_TEXT).createOn(DEFAULT_CREATE_ON);
        return workNotes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkNotes createUpdatedEntity(EntityManager em) {
        WorkNotes workNotes = new WorkNotes().text(UPDATED_TEXT).createOn(UPDATED_CREATE_ON);
        return workNotes;
    }

    @BeforeEach
    public void initTest() {
        workNotes = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkNotes() throws Exception {
        int databaseSizeBeforeCreate = workNotesRepository.findAll().size();
        // Create the WorkNotes
        WorkNotesDTO workNotesDTO = workNotesMapper.toDto(workNotes);
        restWorkNotesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workNotesDTO)))
            .andExpect(status().isCreated());

        // Validate the WorkNotes in the database
        List<WorkNotes> workNotesList = workNotesRepository.findAll();
        assertThat(workNotesList).hasSize(databaseSizeBeforeCreate + 1);
        WorkNotes testWorkNotes = workNotesList.get(workNotesList.size() - 1);
        assertThat(testWorkNotes.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testWorkNotes.getCreateOn()).isEqualTo(DEFAULT_CREATE_ON);
    }

    @Test
    @Transactional
    void createWorkNotesWithExistingId() throws Exception {
        // Create the WorkNotes with an existing ID
        workNotes.setId(1L);
        WorkNotesDTO workNotesDTO = workNotesMapper.toDto(workNotes);

        int databaseSizeBeforeCreate = workNotesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkNotesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workNotesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WorkNotes in the database
        List<WorkNotes> workNotesList = workNotesRepository.findAll();
        assertThat(workNotesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = workNotesRepository.findAll().size();
        // set the field null
        workNotes.setText(null);

        // Create the WorkNotes, which fails.
        WorkNotesDTO workNotesDTO = workNotesMapper.toDto(workNotes);

        restWorkNotesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workNotesDTO)))
            .andExpect(status().isBadRequest());

        List<WorkNotes> workNotesList = workNotesRepository.findAll();
        assertThat(workNotesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreateOnIsRequired() throws Exception {
        int databaseSizeBeforeTest = workNotesRepository.findAll().size();
        // set the field null
        workNotes.setCreateOn(null);

        // Create the WorkNotes, which fails.
        WorkNotesDTO workNotesDTO = workNotesMapper.toDto(workNotes);

        restWorkNotesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workNotesDTO)))
            .andExpect(status().isBadRequest());

        List<WorkNotes> workNotesList = workNotesRepository.findAll();
        assertThat(workNotesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWorkNotes() throws Exception {
        // Initialize the database
        workNotesRepository.saveAndFlush(workNotes);

        // Get all the workNotesList
        restWorkNotesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workNotes.getId().intValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].createOn").value(hasItem(sameInstant(DEFAULT_CREATE_ON))));
    }

    @Test
    @Transactional
    void getWorkNotes() throws Exception {
        // Initialize the database
        workNotesRepository.saveAndFlush(workNotes);

        // Get the workNotes
        restWorkNotesMockMvc
            .perform(get(ENTITY_API_URL_ID, workNotes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workNotes.getId().intValue()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT))
            .andExpect(jsonPath("$.createOn").value(sameInstant(DEFAULT_CREATE_ON)));
    }

    @Test
    @Transactional
    void getNonExistingWorkNotes() throws Exception {
        // Get the workNotes
        restWorkNotesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWorkNotes() throws Exception {
        // Initialize the database
        workNotesRepository.saveAndFlush(workNotes);

        int databaseSizeBeforeUpdate = workNotesRepository.findAll().size();

        // Update the workNotes
        WorkNotes updatedWorkNotes = workNotesRepository.findById(workNotes.getId()).get();
        // Disconnect from session so that the updates on updatedWorkNotes are not directly saved in db
        em.detach(updatedWorkNotes);
        updatedWorkNotes.text(UPDATED_TEXT).createOn(UPDATED_CREATE_ON);
        WorkNotesDTO workNotesDTO = workNotesMapper.toDto(updatedWorkNotes);

        restWorkNotesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workNotesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workNotesDTO))
            )
            .andExpect(status().isOk());

        // Validate the WorkNotes in the database
        List<WorkNotes> workNotesList = workNotesRepository.findAll();
        assertThat(workNotesList).hasSize(databaseSizeBeforeUpdate);
        WorkNotes testWorkNotes = workNotesList.get(workNotesList.size() - 1);
        assertThat(testWorkNotes.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testWorkNotes.getCreateOn()).isEqualTo(UPDATED_CREATE_ON);
    }

    @Test
    @Transactional
    void putNonExistingWorkNotes() throws Exception {
        int databaseSizeBeforeUpdate = workNotesRepository.findAll().size();
        workNotes.setId(count.incrementAndGet());

        // Create the WorkNotes
        WorkNotesDTO workNotesDTO = workNotesMapper.toDto(workNotes);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkNotesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workNotesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workNotesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkNotes in the database
        List<WorkNotes> workNotesList = workNotesRepository.findAll();
        assertThat(workNotesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkNotes() throws Exception {
        int databaseSizeBeforeUpdate = workNotesRepository.findAll().size();
        workNotes.setId(count.incrementAndGet());

        // Create the WorkNotes
        WorkNotesDTO workNotesDTO = workNotesMapper.toDto(workNotes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkNotesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workNotesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkNotes in the database
        List<WorkNotes> workNotesList = workNotesRepository.findAll();
        assertThat(workNotesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkNotes() throws Exception {
        int databaseSizeBeforeUpdate = workNotesRepository.findAll().size();
        workNotes.setId(count.incrementAndGet());

        // Create the WorkNotes
        WorkNotesDTO workNotesDTO = workNotesMapper.toDto(workNotes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkNotesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workNotesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkNotes in the database
        List<WorkNotes> workNotesList = workNotesRepository.findAll();
        assertThat(workNotesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWorkNotesWithPatch() throws Exception {
        // Initialize the database
        workNotesRepository.saveAndFlush(workNotes);

        int databaseSizeBeforeUpdate = workNotesRepository.findAll().size();

        // Update the workNotes using partial update
        WorkNotes partialUpdatedWorkNotes = new WorkNotes();
        partialUpdatedWorkNotes.setId(workNotes.getId());

        partialUpdatedWorkNotes.text(UPDATED_TEXT);

        restWorkNotesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkNotes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkNotes))
            )
            .andExpect(status().isOk());

        // Validate the WorkNotes in the database
        List<WorkNotes> workNotesList = workNotesRepository.findAll();
        assertThat(workNotesList).hasSize(databaseSizeBeforeUpdate);
        WorkNotes testWorkNotes = workNotesList.get(workNotesList.size() - 1);
        assertThat(testWorkNotes.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testWorkNotes.getCreateOn()).isEqualTo(DEFAULT_CREATE_ON);
    }

    @Test
    @Transactional
    void fullUpdateWorkNotesWithPatch() throws Exception {
        // Initialize the database
        workNotesRepository.saveAndFlush(workNotes);

        int databaseSizeBeforeUpdate = workNotesRepository.findAll().size();

        // Update the workNotes using partial update
        WorkNotes partialUpdatedWorkNotes = new WorkNotes();
        partialUpdatedWorkNotes.setId(workNotes.getId());

        partialUpdatedWorkNotes.text(UPDATED_TEXT).createOn(UPDATED_CREATE_ON);

        restWorkNotesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkNotes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkNotes))
            )
            .andExpect(status().isOk());

        // Validate the WorkNotes in the database
        List<WorkNotes> workNotesList = workNotesRepository.findAll();
        assertThat(workNotesList).hasSize(databaseSizeBeforeUpdate);
        WorkNotes testWorkNotes = workNotesList.get(workNotesList.size() - 1);
        assertThat(testWorkNotes.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testWorkNotes.getCreateOn()).isEqualTo(UPDATED_CREATE_ON);
    }

    @Test
    @Transactional
    void patchNonExistingWorkNotes() throws Exception {
        int databaseSizeBeforeUpdate = workNotesRepository.findAll().size();
        workNotes.setId(count.incrementAndGet());

        // Create the WorkNotes
        WorkNotesDTO workNotesDTO = workNotesMapper.toDto(workNotes);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkNotesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workNotesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workNotesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkNotes in the database
        List<WorkNotes> workNotesList = workNotesRepository.findAll();
        assertThat(workNotesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkNotes() throws Exception {
        int databaseSizeBeforeUpdate = workNotesRepository.findAll().size();
        workNotes.setId(count.incrementAndGet());

        // Create the WorkNotes
        WorkNotesDTO workNotesDTO = workNotesMapper.toDto(workNotes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkNotesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workNotesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkNotes in the database
        List<WorkNotes> workNotesList = workNotesRepository.findAll();
        assertThat(workNotesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkNotes() throws Exception {
        int databaseSizeBeforeUpdate = workNotesRepository.findAll().size();
        workNotes.setId(count.incrementAndGet());

        // Create the WorkNotes
        WorkNotesDTO workNotesDTO = workNotesMapper.toDto(workNotes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkNotesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(workNotesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkNotes in the database
        List<WorkNotes> workNotesList = workNotesRepository.findAll();
        assertThat(workNotesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWorkNotes() throws Exception {
        // Initialize the database
        workNotesRepository.saveAndFlush(workNotes);

        int databaseSizeBeforeDelete = workNotesRepository.findAll().size();

        // Delete the workNotes
        restWorkNotesMockMvc
            .perform(delete(ENTITY_API_URL_ID, workNotes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkNotes> workNotesList = workNotesRepository.findAll();
        assertThat(workNotesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
