package com.follo.taskmanager.web.rest;

import static com.follo.taskmanager.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.follo.taskmanager.IntegrationTest;
import com.follo.taskmanager.domain.Task;
import com.follo.taskmanager.repository.TaskRepository;
import com.follo.taskmanager.service.dto.TaskDTO;
import com.follo.taskmanager.service.mapper.TaskMapper;
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
 * Integration tests for the {@link TaskResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TaskResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DUE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DUE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_ESTIMATED_TIME_TO_COMPLETE = 1;
    private static final Integer UPDATED_ESTIMATED_TIME_TO_COMPLETE = 2;

    private static final String DEFAULT_ESTIMATED_TIME_TO_COMPLETE_TIME_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_ESTIMATED_TIME_TO_COMPLETE_TIME_UNIT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_READ_BY_ASSIGN_TO = false;
    private static final Boolean UPDATED_IS_READ_BY_ASSIGN_TO = true;

    private static final Boolean DEFAULT_IS_URGENT = false;
    private static final Boolean UPDATED_IS_URGENT = true;

    private static final Boolean DEFAULT_IS_REJECTED = false;
    private static final Boolean UPDATED_IS_REJECTED = true;

    private static final Boolean DEFAULT_IS_COMPLETED = false;
    private static final Boolean UPDATED_IS_COMPLETED = true;

    private static final ZonedDateTime DEFAULT_COMPLETED_ON = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_COMPLETED_ON = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_REJECTED_ON = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_REJECTED_ON = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_CREATE_ON = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATE_ON = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_ON = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_ON = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/tasks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTaskMockMvc;

    private Task task;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Task createEntity(EntityManager em) {
        Task task = new Task()
            .description(DEFAULT_DESCRIPTION)
            .dueDate(DEFAULT_DUE_DATE)
            .estimatedTimeToComplete(DEFAULT_ESTIMATED_TIME_TO_COMPLETE)
            .estimatedTimeToCompleteTimeUnit(DEFAULT_ESTIMATED_TIME_TO_COMPLETE_TIME_UNIT)
            .isReadByAssignTo(DEFAULT_IS_READ_BY_ASSIGN_TO)
            .isUrgent(DEFAULT_IS_URGENT)
            .isRejected(DEFAULT_IS_REJECTED)
            .isCompleted(DEFAULT_IS_COMPLETED)
            .completedOn(DEFAULT_COMPLETED_ON)
            .rejectedOn(DEFAULT_REJECTED_ON)
            .createOn(DEFAULT_CREATE_ON)
            .updatedOn(DEFAULT_UPDATED_ON);
        return task;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Task createUpdatedEntity(EntityManager em) {
        Task task = new Task()
            .description(UPDATED_DESCRIPTION)
            .dueDate(UPDATED_DUE_DATE)
            .estimatedTimeToComplete(UPDATED_ESTIMATED_TIME_TO_COMPLETE)
            .estimatedTimeToCompleteTimeUnit(UPDATED_ESTIMATED_TIME_TO_COMPLETE_TIME_UNIT)
            .isReadByAssignTo(UPDATED_IS_READ_BY_ASSIGN_TO)
            .isUrgent(UPDATED_IS_URGENT)
            .isRejected(UPDATED_IS_REJECTED)
            .isCompleted(UPDATED_IS_COMPLETED)
            .completedOn(UPDATED_COMPLETED_ON)
            .rejectedOn(UPDATED_REJECTED_ON)
            .createOn(UPDATED_CREATE_ON)
            .updatedOn(UPDATED_UPDATED_ON);
        return task;
    }

    @BeforeEach
    public void initTest() {
        task = createEntity(em);
    }

    @Test
    @Transactional
    void createTask() throws Exception {
        int databaseSizeBeforeCreate = taskRepository.findAll().size();
        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);
        restTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskDTO)))
            .andExpect(status().isCreated());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeCreate + 1);
        Task testTask = taskList.get(taskList.size() - 1);
        assertThat(testTask.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTask.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
        assertThat(testTask.getEstimatedTimeToComplete()).isEqualTo(DEFAULT_ESTIMATED_TIME_TO_COMPLETE);
        assertThat(testTask.getEstimatedTimeToCompleteTimeUnit()).isEqualTo(DEFAULT_ESTIMATED_TIME_TO_COMPLETE_TIME_UNIT);
        assertThat(testTask.getIsReadByAssignTo()).isEqualTo(DEFAULT_IS_READ_BY_ASSIGN_TO);
        assertThat(testTask.getIsUrgent()).isEqualTo(DEFAULT_IS_URGENT);
        assertThat(testTask.getIsRejected()).isEqualTo(DEFAULT_IS_REJECTED);
        assertThat(testTask.getIsCompleted()).isEqualTo(DEFAULT_IS_COMPLETED);
        assertThat(testTask.getCompletedOn()).isEqualTo(DEFAULT_COMPLETED_ON);
        assertThat(testTask.getRejectedOn()).isEqualTo(DEFAULT_REJECTED_ON);
        assertThat(testTask.getCreateOn()).isEqualTo(DEFAULT_CREATE_ON);
        assertThat(testTask.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
    }

    @Test
    @Transactional
    void createTaskWithExistingId() throws Exception {
        // Create the Task with an existing ID
        task.setId(1L);
        TaskDTO taskDTO = taskMapper.toDto(task);

        int databaseSizeBeforeCreate = taskRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTasks() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList
        restTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(task.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(sameInstant(DEFAULT_DUE_DATE))))
            .andExpect(jsonPath("$.[*].estimatedTimeToComplete").value(hasItem(DEFAULT_ESTIMATED_TIME_TO_COMPLETE)))
            .andExpect(jsonPath("$.[*].estimatedTimeToCompleteTimeUnit").value(hasItem(DEFAULT_ESTIMATED_TIME_TO_COMPLETE_TIME_UNIT)))
            .andExpect(jsonPath("$.[*].isReadByAssignTo").value(hasItem(DEFAULT_IS_READ_BY_ASSIGN_TO.booleanValue())))
            .andExpect(jsonPath("$.[*].isUrgent").value(hasItem(DEFAULT_IS_URGENT.booleanValue())))
            .andExpect(jsonPath("$.[*].isRejected").value(hasItem(DEFAULT_IS_REJECTED.booleanValue())))
            .andExpect(jsonPath("$.[*].isCompleted").value(hasItem(DEFAULT_IS_COMPLETED.booleanValue())))
            .andExpect(jsonPath("$.[*].completedOn").value(hasItem(sameInstant(DEFAULT_COMPLETED_ON))))
            .andExpect(jsonPath("$.[*].rejectedOn").value(hasItem(sameInstant(DEFAULT_REJECTED_ON))))
            .andExpect(jsonPath("$.[*].createOn").value(hasItem(sameInstant(DEFAULT_CREATE_ON))))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(sameInstant(DEFAULT_UPDATED_ON))));
    }

    @Test
    @Transactional
    void getTask() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get the task
        restTaskMockMvc
            .perform(get(ENTITY_API_URL_ID, task.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(task.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.dueDate").value(sameInstant(DEFAULT_DUE_DATE)))
            .andExpect(jsonPath("$.estimatedTimeToComplete").value(DEFAULT_ESTIMATED_TIME_TO_COMPLETE))
            .andExpect(jsonPath("$.estimatedTimeToCompleteTimeUnit").value(DEFAULT_ESTIMATED_TIME_TO_COMPLETE_TIME_UNIT))
            .andExpect(jsonPath("$.isReadByAssignTo").value(DEFAULT_IS_READ_BY_ASSIGN_TO.booleanValue()))
            .andExpect(jsonPath("$.isUrgent").value(DEFAULT_IS_URGENT.booleanValue()))
            .andExpect(jsonPath("$.isRejected").value(DEFAULT_IS_REJECTED.booleanValue()))
            .andExpect(jsonPath("$.isCompleted").value(DEFAULT_IS_COMPLETED.booleanValue()))
            .andExpect(jsonPath("$.completedOn").value(sameInstant(DEFAULT_COMPLETED_ON)))
            .andExpect(jsonPath("$.rejectedOn").value(sameInstant(DEFAULT_REJECTED_ON)))
            .andExpect(jsonPath("$.createOn").value(sameInstant(DEFAULT_CREATE_ON)))
            .andExpect(jsonPath("$.updatedOn").value(sameInstant(DEFAULT_UPDATED_ON)));
    }

    @Test
    @Transactional
    void getNonExistingTask() throws Exception {
        // Get the task
        restTaskMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTask() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        int databaseSizeBeforeUpdate = taskRepository.findAll().size();

        // Update the task
        Task updatedTask = taskRepository.findById(task.getId()).get();
        // Disconnect from session so that the updates on updatedTask are not directly saved in db
        em.detach(updatedTask);
        updatedTask
            .description(UPDATED_DESCRIPTION)
            .dueDate(UPDATED_DUE_DATE)
            .estimatedTimeToComplete(UPDATED_ESTIMATED_TIME_TO_COMPLETE)
            .estimatedTimeToCompleteTimeUnit(UPDATED_ESTIMATED_TIME_TO_COMPLETE_TIME_UNIT)
            .isReadByAssignTo(UPDATED_IS_READ_BY_ASSIGN_TO)
            .isUrgent(UPDATED_IS_URGENT)
            .isRejected(UPDATED_IS_REJECTED)
            .isCompleted(UPDATED_IS_COMPLETED)
            .completedOn(UPDATED_COMPLETED_ON)
            .rejectedOn(UPDATED_REJECTED_ON)
            .createOn(UPDATED_CREATE_ON)
            .updatedOn(UPDATED_UPDATED_ON);
        TaskDTO taskDTO = taskMapper.toDto(updatedTask);

        restTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taskDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taskDTO))
            )
            .andExpect(status().isOk());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
        Task testTask = taskList.get(taskList.size() - 1);
        assertThat(testTask.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTask.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testTask.getEstimatedTimeToComplete()).isEqualTo(UPDATED_ESTIMATED_TIME_TO_COMPLETE);
        assertThat(testTask.getEstimatedTimeToCompleteTimeUnit()).isEqualTo(UPDATED_ESTIMATED_TIME_TO_COMPLETE_TIME_UNIT);
        assertThat(testTask.getIsReadByAssignTo()).isEqualTo(UPDATED_IS_READ_BY_ASSIGN_TO);
        assertThat(testTask.getIsUrgent()).isEqualTo(UPDATED_IS_URGENT);
        assertThat(testTask.getIsRejected()).isEqualTo(UPDATED_IS_REJECTED);
        assertThat(testTask.getIsCompleted()).isEqualTo(UPDATED_IS_COMPLETED);
        assertThat(testTask.getCompletedOn()).isEqualTo(UPDATED_COMPLETED_ON);
        assertThat(testTask.getRejectedOn()).isEqualTo(UPDATED_REJECTED_ON);
        assertThat(testTask.getCreateOn()).isEqualTo(UPDATED_CREATE_ON);
        assertThat(testTask.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
    }

    @Test
    @Transactional
    void putNonExistingTask() throws Exception {
        int databaseSizeBeforeUpdate = taskRepository.findAll().size();
        task.setId(count.incrementAndGet());

        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taskDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTask() throws Exception {
        int databaseSizeBeforeUpdate = taskRepository.findAll().size();
        task.setId(count.incrementAndGet());

        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTask() throws Exception {
        int databaseSizeBeforeUpdate = taskRepository.findAll().size();
        task.setId(count.incrementAndGet());

        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTaskWithPatch() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        int databaseSizeBeforeUpdate = taskRepository.findAll().size();

        // Update the task using partial update
        Task partialUpdatedTask = new Task();
        partialUpdatedTask.setId(task.getId());

        partialUpdatedTask
            .dueDate(UPDATED_DUE_DATE)
            .estimatedTimeToCompleteTimeUnit(UPDATED_ESTIMATED_TIME_TO_COMPLETE_TIME_UNIT)
            .isCompleted(UPDATED_IS_COMPLETED)
            .completedOn(UPDATED_COMPLETED_ON)
            .rejectedOn(UPDATED_REJECTED_ON)
            .updatedOn(UPDATED_UPDATED_ON);

        restTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTask.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTask))
            )
            .andExpect(status().isOk());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
        Task testTask = taskList.get(taskList.size() - 1);
        assertThat(testTask.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTask.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testTask.getEstimatedTimeToComplete()).isEqualTo(DEFAULT_ESTIMATED_TIME_TO_COMPLETE);
        assertThat(testTask.getEstimatedTimeToCompleteTimeUnit()).isEqualTo(UPDATED_ESTIMATED_TIME_TO_COMPLETE_TIME_UNIT);
        assertThat(testTask.getIsReadByAssignTo()).isEqualTo(DEFAULT_IS_READ_BY_ASSIGN_TO);
        assertThat(testTask.getIsUrgent()).isEqualTo(DEFAULT_IS_URGENT);
        assertThat(testTask.getIsRejected()).isEqualTo(DEFAULT_IS_REJECTED);
        assertThat(testTask.getIsCompleted()).isEqualTo(UPDATED_IS_COMPLETED);
        assertThat(testTask.getCompletedOn()).isEqualTo(UPDATED_COMPLETED_ON);
        assertThat(testTask.getRejectedOn()).isEqualTo(UPDATED_REJECTED_ON);
        assertThat(testTask.getCreateOn()).isEqualTo(DEFAULT_CREATE_ON);
        assertThat(testTask.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
    }

    @Test
    @Transactional
    void fullUpdateTaskWithPatch() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        int databaseSizeBeforeUpdate = taskRepository.findAll().size();

        // Update the task using partial update
        Task partialUpdatedTask = new Task();
        partialUpdatedTask.setId(task.getId());

        partialUpdatedTask
            .description(UPDATED_DESCRIPTION)
            .dueDate(UPDATED_DUE_DATE)
            .estimatedTimeToComplete(UPDATED_ESTIMATED_TIME_TO_COMPLETE)
            .estimatedTimeToCompleteTimeUnit(UPDATED_ESTIMATED_TIME_TO_COMPLETE_TIME_UNIT)
            .isReadByAssignTo(UPDATED_IS_READ_BY_ASSIGN_TO)
            .isUrgent(UPDATED_IS_URGENT)
            .isRejected(UPDATED_IS_REJECTED)
            .isCompleted(UPDATED_IS_COMPLETED)
            .completedOn(UPDATED_COMPLETED_ON)
            .rejectedOn(UPDATED_REJECTED_ON)
            .createOn(UPDATED_CREATE_ON)
            .updatedOn(UPDATED_UPDATED_ON);

        restTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTask.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTask))
            )
            .andExpect(status().isOk());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
        Task testTask = taskList.get(taskList.size() - 1);
        assertThat(testTask.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTask.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testTask.getEstimatedTimeToComplete()).isEqualTo(UPDATED_ESTIMATED_TIME_TO_COMPLETE);
        assertThat(testTask.getEstimatedTimeToCompleteTimeUnit()).isEqualTo(UPDATED_ESTIMATED_TIME_TO_COMPLETE_TIME_UNIT);
        assertThat(testTask.getIsReadByAssignTo()).isEqualTo(UPDATED_IS_READ_BY_ASSIGN_TO);
        assertThat(testTask.getIsUrgent()).isEqualTo(UPDATED_IS_URGENT);
        assertThat(testTask.getIsRejected()).isEqualTo(UPDATED_IS_REJECTED);
        assertThat(testTask.getIsCompleted()).isEqualTo(UPDATED_IS_COMPLETED);
        assertThat(testTask.getCompletedOn()).isEqualTo(UPDATED_COMPLETED_ON);
        assertThat(testTask.getRejectedOn()).isEqualTo(UPDATED_REJECTED_ON);
        assertThat(testTask.getCreateOn()).isEqualTo(UPDATED_CREATE_ON);
        assertThat(testTask.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
    }

    @Test
    @Transactional
    void patchNonExistingTask() throws Exception {
        int databaseSizeBeforeUpdate = taskRepository.findAll().size();
        task.setId(count.incrementAndGet());

        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, taskDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTask() throws Exception {
        int databaseSizeBeforeUpdate = taskRepository.findAll().size();
        task.setId(count.incrementAndGet());

        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTask() throws Exception {
        int databaseSizeBeforeUpdate = taskRepository.findAll().size();
        task.setId(count.incrementAndGet());

        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(taskDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTask() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        int databaseSizeBeforeDelete = taskRepository.findAll().size();

        // Delete the task
        restTaskMockMvc
            .perform(delete(ENTITY_API_URL_ID, task.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
