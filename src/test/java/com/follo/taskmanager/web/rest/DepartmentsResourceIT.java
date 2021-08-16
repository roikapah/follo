package com.follo.taskmanager.web.rest;

import static com.follo.taskmanager.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.follo.taskmanager.IntegrationTest;
import com.follo.taskmanager.domain.Departments;
import com.follo.taskmanager.repository.DepartmentsRepository;
import com.follo.taskmanager.service.dto.DepartmentsDTO;
import com.follo.taskmanager.service.mapper.DepartmentsMapper;
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
 * Integration tests for the {@link DepartmentsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DepartmentsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATE_ON = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATE_ON = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_ON = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_ON = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/departments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DepartmentsRepository departmentsRepository;

    @Autowired
    private DepartmentsMapper departmentsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepartmentsMockMvc;

    private Departments departments;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Departments createEntity(EntityManager em) {
        Departments departments = new Departments().name(DEFAULT_NAME).createOn(DEFAULT_CREATE_ON).updatedOn(DEFAULT_UPDATED_ON);
        return departments;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Departments createUpdatedEntity(EntityManager em) {
        Departments departments = new Departments().name(UPDATED_NAME).createOn(UPDATED_CREATE_ON).updatedOn(UPDATED_UPDATED_ON);
        return departments;
    }

    @BeforeEach
    public void initTest() {
        departments = createEntity(em);
    }

    @Test
    @Transactional
    void createDepartments() throws Exception {
        int databaseSizeBeforeCreate = departmentsRepository.findAll().size();
        // Create the Departments
        DepartmentsDTO departmentsDTO = departmentsMapper.toDto(departments);
        restDepartmentsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(departmentsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Departments in the database
        List<Departments> departmentsList = departmentsRepository.findAll();
        assertThat(departmentsList).hasSize(databaseSizeBeforeCreate + 1);
        Departments testDepartments = departmentsList.get(departmentsList.size() - 1);
        assertThat(testDepartments.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDepartments.getCreateOn()).isEqualTo(DEFAULT_CREATE_ON);
        assertThat(testDepartments.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
    }

    @Test
    @Transactional
    void createDepartmentsWithExistingId() throws Exception {
        // Create the Departments with an existing ID
        departments.setId(1L);
        DepartmentsDTO departmentsDTO = departmentsMapper.toDto(departments);

        int databaseSizeBeforeCreate = departmentsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepartmentsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(departmentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departments in the database
        List<Departments> departmentsList = departmentsRepository.findAll();
        assertThat(departmentsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = departmentsRepository.findAll().size();
        // set the field null
        departments.setName(null);

        // Create the Departments, which fails.
        DepartmentsDTO departmentsDTO = departmentsMapper.toDto(departments);

        restDepartmentsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(departmentsDTO))
            )
            .andExpect(status().isBadRequest());

        List<Departments> departmentsList = departmentsRepository.findAll();
        assertThat(departmentsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDepartments() throws Exception {
        // Initialize the database
        departmentsRepository.saveAndFlush(departments);

        // Get all the departmentsList
        restDepartmentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(departments.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createOn").value(hasItem(sameInstant(DEFAULT_CREATE_ON))))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(sameInstant(DEFAULT_UPDATED_ON))));
    }

    @Test
    @Transactional
    void getDepartments() throws Exception {
        // Initialize the database
        departmentsRepository.saveAndFlush(departments);

        // Get the departments
        restDepartmentsMockMvc
            .perform(get(ENTITY_API_URL_ID, departments.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(departments.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.createOn").value(sameInstant(DEFAULT_CREATE_ON)))
            .andExpect(jsonPath("$.updatedOn").value(sameInstant(DEFAULT_UPDATED_ON)));
    }

    @Test
    @Transactional
    void getNonExistingDepartments() throws Exception {
        // Get the departments
        restDepartmentsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDepartments() throws Exception {
        // Initialize the database
        departmentsRepository.saveAndFlush(departments);

        int databaseSizeBeforeUpdate = departmentsRepository.findAll().size();

        // Update the departments
        Departments updatedDepartments = departmentsRepository.findById(departments.getId()).get();
        // Disconnect from session so that the updates on updatedDepartments are not directly saved in db
        em.detach(updatedDepartments);
        updatedDepartments.name(UPDATED_NAME).createOn(UPDATED_CREATE_ON).updatedOn(UPDATED_UPDATED_ON);
        DepartmentsDTO departmentsDTO = departmentsMapper.toDto(updatedDepartments);

        restDepartmentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, departmentsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(departmentsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Departments in the database
        List<Departments> departmentsList = departmentsRepository.findAll();
        assertThat(departmentsList).hasSize(databaseSizeBeforeUpdate);
        Departments testDepartments = departmentsList.get(departmentsList.size() - 1);
        assertThat(testDepartments.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDepartments.getCreateOn()).isEqualTo(UPDATED_CREATE_ON);
        assertThat(testDepartments.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
    }

    @Test
    @Transactional
    void putNonExistingDepartments() throws Exception {
        int databaseSizeBeforeUpdate = departmentsRepository.findAll().size();
        departments.setId(count.incrementAndGet());

        // Create the Departments
        DepartmentsDTO departmentsDTO = departmentsMapper.toDto(departments);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartmentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, departmentsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(departmentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departments in the database
        List<Departments> departmentsList = departmentsRepository.findAll();
        assertThat(departmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDepartments() throws Exception {
        int databaseSizeBeforeUpdate = departmentsRepository.findAll().size();
        departments.setId(count.incrementAndGet());

        // Create the Departments
        DepartmentsDTO departmentsDTO = departmentsMapper.toDto(departments);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartmentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(departmentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departments in the database
        List<Departments> departmentsList = departmentsRepository.findAll();
        assertThat(departmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDepartments() throws Exception {
        int databaseSizeBeforeUpdate = departmentsRepository.findAll().size();
        departments.setId(count.incrementAndGet());

        // Create the Departments
        DepartmentsDTO departmentsDTO = departmentsMapper.toDto(departments);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartmentsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(departmentsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Departments in the database
        List<Departments> departmentsList = departmentsRepository.findAll();
        assertThat(departmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDepartmentsWithPatch() throws Exception {
        // Initialize the database
        departmentsRepository.saveAndFlush(departments);

        int databaseSizeBeforeUpdate = departmentsRepository.findAll().size();

        // Update the departments using partial update
        Departments partialUpdatedDepartments = new Departments();
        partialUpdatedDepartments.setId(departments.getId());

        partialUpdatedDepartments.createOn(UPDATED_CREATE_ON).updatedOn(UPDATED_UPDATED_ON);

        restDepartmentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepartments.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepartments))
            )
            .andExpect(status().isOk());

        // Validate the Departments in the database
        List<Departments> departmentsList = departmentsRepository.findAll();
        assertThat(departmentsList).hasSize(databaseSizeBeforeUpdate);
        Departments testDepartments = departmentsList.get(departmentsList.size() - 1);
        assertThat(testDepartments.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDepartments.getCreateOn()).isEqualTo(UPDATED_CREATE_ON);
        assertThat(testDepartments.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
    }

    @Test
    @Transactional
    void fullUpdateDepartmentsWithPatch() throws Exception {
        // Initialize the database
        departmentsRepository.saveAndFlush(departments);

        int databaseSizeBeforeUpdate = departmentsRepository.findAll().size();

        // Update the departments using partial update
        Departments partialUpdatedDepartments = new Departments();
        partialUpdatedDepartments.setId(departments.getId());

        partialUpdatedDepartments.name(UPDATED_NAME).createOn(UPDATED_CREATE_ON).updatedOn(UPDATED_UPDATED_ON);

        restDepartmentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepartments.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepartments))
            )
            .andExpect(status().isOk());

        // Validate the Departments in the database
        List<Departments> departmentsList = departmentsRepository.findAll();
        assertThat(departmentsList).hasSize(databaseSizeBeforeUpdate);
        Departments testDepartments = departmentsList.get(departmentsList.size() - 1);
        assertThat(testDepartments.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDepartments.getCreateOn()).isEqualTo(UPDATED_CREATE_ON);
        assertThat(testDepartments.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
    }

    @Test
    @Transactional
    void patchNonExistingDepartments() throws Exception {
        int databaseSizeBeforeUpdate = departmentsRepository.findAll().size();
        departments.setId(count.incrementAndGet());

        // Create the Departments
        DepartmentsDTO departmentsDTO = departmentsMapper.toDto(departments);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartmentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, departmentsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(departmentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departments in the database
        List<Departments> departmentsList = departmentsRepository.findAll();
        assertThat(departmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDepartments() throws Exception {
        int databaseSizeBeforeUpdate = departmentsRepository.findAll().size();
        departments.setId(count.incrementAndGet());

        // Create the Departments
        DepartmentsDTO departmentsDTO = departmentsMapper.toDto(departments);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartmentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(departmentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departments in the database
        List<Departments> departmentsList = departmentsRepository.findAll();
        assertThat(departmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDepartments() throws Exception {
        int databaseSizeBeforeUpdate = departmentsRepository.findAll().size();
        departments.setId(count.incrementAndGet());

        // Create the Departments
        DepartmentsDTO departmentsDTO = departmentsMapper.toDto(departments);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartmentsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(departmentsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Departments in the database
        List<Departments> departmentsList = departmentsRepository.findAll();
        assertThat(departmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDepartments() throws Exception {
        // Initialize the database
        departmentsRepository.saveAndFlush(departments);

        int databaseSizeBeforeDelete = departmentsRepository.findAll().size();

        // Delete the departments
        restDepartmentsMockMvc
            .perform(delete(ENTITY_API_URL_ID, departments.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Departments> departmentsList = departmentsRepository.findAll();
        assertThat(departmentsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
