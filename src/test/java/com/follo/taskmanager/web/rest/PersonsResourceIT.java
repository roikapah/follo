package com.follo.taskmanager.web.rest;

import static com.follo.taskmanager.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.follo.taskmanager.IntegrationTest;
import com.follo.taskmanager.domain.Persons;
import com.follo.taskmanager.domain.enumeration.Role;
import com.follo.taskmanager.repository.PersonsRepository;
import com.follo.taskmanager.service.dto.PersonsDTO;
import com.follo.taskmanager.service.mapper.PersonsMapper;
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
 * Integration tests for the {@link PersonsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PersonsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Role DEFAULT_ROLE = Role.WORKER;
    private static final Role UPDATED_ROLE = Role.MANAGER;

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATE_ON = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATE_ON = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_ON = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_ON = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/persons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PersonsRepository personsRepository;

    @Autowired
    private PersonsMapper personsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonsMockMvc;

    private Persons persons;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Persons createEntity(EntityManager em) {
        Persons persons = new Persons()
            .name(DEFAULT_NAME)
            .email(DEFAULT_EMAIL)
            .role(DEFAULT_ROLE)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .address(DEFAULT_ADDRESS)
            .createOn(DEFAULT_CREATE_ON)
            .updatedOn(DEFAULT_UPDATED_ON);
        return persons;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Persons createUpdatedEntity(EntityManager em) {
        Persons persons = new Persons()
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .role(UPDATED_ROLE)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .address(UPDATED_ADDRESS)
            .createOn(UPDATED_CREATE_ON)
            .updatedOn(UPDATED_UPDATED_ON);
        return persons;
    }

    @BeforeEach
    public void initTest() {
        persons = createEntity(em);
    }

    @Test
    @Transactional
    void createPersons() throws Exception {
        int databaseSizeBeforeCreate = personsRepository.findAll().size();
        // Create the Persons
        PersonsDTO personsDTO = personsMapper.toDto(persons);
        restPersonsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personsDTO)))
            .andExpect(status().isCreated());

        // Validate the Persons in the database
        List<Persons> personsList = personsRepository.findAll();
        assertThat(personsList).hasSize(databaseSizeBeforeCreate + 1);
        Persons testPersons = personsList.get(personsList.size() - 1);
        assertThat(testPersons.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPersons.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPersons.getRole()).isEqualTo(DEFAULT_ROLE);
        assertThat(testPersons.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testPersons.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testPersons.getCreateOn()).isEqualTo(DEFAULT_CREATE_ON);
        assertThat(testPersons.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
    }

    @Test
    @Transactional
    void createPersonsWithExistingId() throws Exception {
        // Create the Persons with an existing ID
        persons.setId(1L);
        PersonsDTO personsDTO = personsMapper.toDto(persons);

        int databaseSizeBeforeCreate = personsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Persons in the database
        List<Persons> personsList = personsRepository.findAll();
        assertThat(personsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = personsRepository.findAll().size();
        // set the field null
        persons.setEmail(null);

        // Create the Persons, which fails.
        PersonsDTO personsDTO = personsMapper.toDto(persons);

        restPersonsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personsDTO)))
            .andExpect(status().isBadRequest());

        List<Persons> personsList = personsRepository.findAll();
        assertThat(personsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRoleIsRequired() throws Exception {
        int databaseSizeBeforeTest = personsRepository.findAll().size();
        // set the field null
        persons.setRole(null);

        // Create the Persons, which fails.
        PersonsDTO personsDTO = personsMapper.toDto(persons);

        restPersonsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personsDTO)))
            .andExpect(status().isBadRequest());

        List<Persons> personsList = personsRepository.findAll();
        assertThat(personsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = personsRepository.findAll().size();
        // set the field null
        persons.setPhoneNumber(null);

        // Create the Persons, which fails.
        PersonsDTO personsDTO = personsMapper.toDto(persons);

        restPersonsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personsDTO)))
            .andExpect(status().isBadRequest());

        List<Persons> personsList = personsRepository.findAll();
        assertThat(personsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPersons() throws Exception {
        // Initialize the database
        personsRepository.saveAndFlush(persons);

        // Get all the personsList
        restPersonsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(persons.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].createOn").value(hasItem(sameInstant(DEFAULT_CREATE_ON))))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(sameInstant(DEFAULT_UPDATED_ON))));
    }

    @Test
    @Transactional
    void getPersons() throws Exception {
        // Initialize the database
        personsRepository.saveAndFlush(persons);

        // Get the persons
        restPersonsMockMvc
            .perform(get(ENTITY_API_URL_ID, persons.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(persons.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.createOn").value(sameInstant(DEFAULT_CREATE_ON)))
            .andExpect(jsonPath("$.updatedOn").value(sameInstant(DEFAULT_UPDATED_ON)));
    }

    @Test
    @Transactional
    void getNonExistingPersons() throws Exception {
        // Get the persons
        restPersonsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPersons() throws Exception {
        // Initialize the database
        personsRepository.saveAndFlush(persons);

        int databaseSizeBeforeUpdate = personsRepository.findAll().size();

        // Update the persons
        Persons updatedPersons = personsRepository.findById(persons.getId()).get();
        // Disconnect from session so that the updates on updatedPersons are not directly saved in db
        em.detach(updatedPersons);
        updatedPersons
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .role(UPDATED_ROLE)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .address(UPDATED_ADDRESS)
            .createOn(UPDATED_CREATE_ON)
            .updatedOn(UPDATED_UPDATED_ON);
        PersonsDTO personsDTO = personsMapper.toDto(updatedPersons);

        restPersonsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Persons in the database
        List<Persons> personsList = personsRepository.findAll();
        assertThat(personsList).hasSize(databaseSizeBeforeUpdate);
        Persons testPersons = personsList.get(personsList.size() - 1);
        assertThat(testPersons.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPersons.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPersons.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testPersons.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testPersons.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testPersons.getCreateOn()).isEqualTo(UPDATED_CREATE_ON);
        assertThat(testPersons.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
    }

    @Test
    @Transactional
    void putNonExistingPersons() throws Exception {
        int databaseSizeBeforeUpdate = personsRepository.findAll().size();
        persons.setId(count.incrementAndGet());

        // Create the Persons
        PersonsDTO personsDTO = personsMapper.toDto(persons);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Persons in the database
        List<Persons> personsList = personsRepository.findAll();
        assertThat(personsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersons() throws Exception {
        int databaseSizeBeforeUpdate = personsRepository.findAll().size();
        persons.setId(count.incrementAndGet());

        // Create the Persons
        PersonsDTO personsDTO = personsMapper.toDto(persons);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Persons in the database
        List<Persons> personsList = personsRepository.findAll();
        assertThat(personsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersons() throws Exception {
        int databaseSizeBeforeUpdate = personsRepository.findAll().size();
        persons.setId(count.incrementAndGet());

        // Create the Persons
        PersonsDTO personsDTO = personsMapper.toDto(persons);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Persons in the database
        List<Persons> personsList = personsRepository.findAll();
        assertThat(personsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonsWithPatch() throws Exception {
        // Initialize the database
        personsRepository.saveAndFlush(persons);

        int databaseSizeBeforeUpdate = personsRepository.findAll().size();

        // Update the persons using partial update
        Persons partialUpdatedPersons = new Persons();
        partialUpdatedPersons.setId(persons.getId());

        partialUpdatedPersons.name(UPDATED_NAME).role(UPDATED_ROLE).address(UPDATED_ADDRESS);

        restPersonsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersons.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersons))
            )
            .andExpect(status().isOk());

        // Validate the Persons in the database
        List<Persons> personsList = personsRepository.findAll();
        assertThat(personsList).hasSize(databaseSizeBeforeUpdate);
        Persons testPersons = personsList.get(personsList.size() - 1);
        assertThat(testPersons.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPersons.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPersons.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testPersons.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testPersons.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testPersons.getCreateOn()).isEqualTo(DEFAULT_CREATE_ON);
        assertThat(testPersons.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
    }

    @Test
    @Transactional
    void fullUpdatePersonsWithPatch() throws Exception {
        // Initialize the database
        personsRepository.saveAndFlush(persons);

        int databaseSizeBeforeUpdate = personsRepository.findAll().size();

        // Update the persons using partial update
        Persons partialUpdatedPersons = new Persons();
        partialUpdatedPersons.setId(persons.getId());

        partialUpdatedPersons
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .role(UPDATED_ROLE)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .address(UPDATED_ADDRESS)
            .createOn(UPDATED_CREATE_ON)
            .updatedOn(UPDATED_UPDATED_ON);

        restPersonsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersons.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersons))
            )
            .andExpect(status().isOk());

        // Validate the Persons in the database
        List<Persons> personsList = personsRepository.findAll();
        assertThat(personsList).hasSize(databaseSizeBeforeUpdate);
        Persons testPersons = personsList.get(personsList.size() - 1);
        assertThat(testPersons.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPersons.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPersons.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testPersons.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testPersons.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testPersons.getCreateOn()).isEqualTo(UPDATED_CREATE_ON);
        assertThat(testPersons.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
    }

    @Test
    @Transactional
    void patchNonExistingPersons() throws Exception {
        int databaseSizeBeforeUpdate = personsRepository.findAll().size();
        persons.setId(count.incrementAndGet());

        // Create the Persons
        PersonsDTO personsDTO = personsMapper.toDto(persons);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Persons in the database
        List<Persons> personsList = personsRepository.findAll();
        assertThat(personsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPersons() throws Exception {
        int databaseSizeBeforeUpdate = personsRepository.findAll().size();
        persons.setId(count.incrementAndGet());

        // Create the Persons
        PersonsDTO personsDTO = personsMapper.toDto(persons);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Persons in the database
        List<Persons> personsList = personsRepository.findAll();
        assertThat(personsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPersons() throws Exception {
        int databaseSizeBeforeUpdate = personsRepository.findAll().size();
        persons.setId(count.incrementAndGet());

        // Create the Persons
        PersonsDTO personsDTO = personsMapper.toDto(persons);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(personsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Persons in the database
        List<Persons> personsList = personsRepository.findAll();
        assertThat(personsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePersons() throws Exception {
        // Initialize the database
        personsRepository.saveAndFlush(persons);

        int databaseSizeBeforeDelete = personsRepository.findAll().size();

        // Delete the persons
        restPersonsMockMvc
            .perform(delete(ENTITY_API_URL_ID, persons.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Persons> personsList = personsRepository.findAll();
        assertThat(personsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
