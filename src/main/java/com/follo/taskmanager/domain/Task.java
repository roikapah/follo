package com.follo.taskmanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Task.
 */
@Entity
@Table(name = "task")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "due_date")
    private ZonedDateTime dueDate;

    @Column(name = "estimated_time_to_complete")
    private Integer estimatedTimeToComplete;

    @Column(name = "estimated_time_to_complete_time_unit")
    private String estimatedTimeToCompleteTimeUnit;

    @Column(name = "is_read_by_assign_to")
    private Boolean isReadByAssignTo;

    @Column(name = "is_urgent")
    private Boolean isUrgent;

    @Column(name = "is_rejected")
    private Boolean isRejected;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @Column(name = "completed_on")
    private ZonedDateTime completedOn;

    @Column(name = "rejected_on")
    private ZonedDateTime rejectedOn;

    @Column(name = "create_on")
    private ZonedDateTime createOn;

    @Column(name = "updated_on")
    private ZonedDateTime updatedOn;

    @JsonIgnoreProperties(value = { "department" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Persons assignTo;

    @OneToOne
    @JoinColumn(unique = true)
    private Departments department;

    @OneToOne
    @JoinColumn(unique = true)
    private Area area;

    @OneToOne
    @JoinColumn(unique = true)
    private TaskType type;

    @OneToMany(mappedBy = "task")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "createBy", "task" }, allowSetters = true)
    private Set<WorkNotes> workNotes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Task id(Long id) {
        this.id = id;
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public Task description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getDueDate() {
        return this.dueDate;
    }

    public Task dueDate(ZonedDateTime dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public void setDueDate(ZonedDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getEstimatedTimeToComplete() {
        return this.estimatedTimeToComplete;
    }

    public Task estimatedTimeToComplete(Integer estimatedTimeToComplete) {
        this.estimatedTimeToComplete = estimatedTimeToComplete;
        return this;
    }

    public void setEstimatedTimeToComplete(Integer estimatedTimeToComplete) {
        this.estimatedTimeToComplete = estimatedTimeToComplete;
    }

    public String getEstimatedTimeToCompleteTimeUnit() {
        return this.estimatedTimeToCompleteTimeUnit;
    }

    public Task estimatedTimeToCompleteTimeUnit(String estimatedTimeToCompleteTimeUnit) {
        this.estimatedTimeToCompleteTimeUnit = estimatedTimeToCompleteTimeUnit;
        return this;
    }

    public void setEstimatedTimeToCompleteTimeUnit(String estimatedTimeToCompleteTimeUnit) {
        this.estimatedTimeToCompleteTimeUnit = estimatedTimeToCompleteTimeUnit;
    }

    public Boolean getIsReadByAssignTo() {
        return this.isReadByAssignTo;
    }

    public Task isReadByAssignTo(Boolean isReadByAssignTo) {
        this.isReadByAssignTo = isReadByAssignTo;
        return this;
    }

    public void setIsReadByAssignTo(Boolean isReadByAssignTo) {
        this.isReadByAssignTo = isReadByAssignTo;
    }

    public Boolean getIsUrgent() {
        return this.isUrgent;
    }

    public Task isUrgent(Boolean isUrgent) {
        this.isUrgent = isUrgent;
        return this;
    }

    public void setIsUrgent(Boolean isUrgent) {
        this.isUrgent = isUrgent;
    }

    public Boolean getIsRejected() {
        return this.isRejected;
    }

    public Task isRejected(Boolean isRejected) {
        this.isRejected = isRejected;
        return this;
    }

    public void setIsRejected(Boolean isRejected) {
        this.isRejected = isRejected;
    }

    public Boolean getIsCompleted() {
        return this.isCompleted;
    }

    public Task isCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
        return this;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public ZonedDateTime getCompletedOn() {
        return this.completedOn;
    }

    public Task completedOn(ZonedDateTime completedOn) {
        this.completedOn = completedOn;
        return this;
    }

    public void setCompletedOn(ZonedDateTime completedOn) {
        this.completedOn = completedOn;
    }

    public ZonedDateTime getRejectedOn() {
        return this.rejectedOn;
    }

    public Task rejectedOn(ZonedDateTime rejectedOn) {
        this.rejectedOn = rejectedOn;
        return this;
    }

    public void setRejectedOn(ZonedDateTime rejectedOn) {
        this.rejectedOn = rejectedOn;
    }

    public ZonedDateTime getCreateOn() {
        return this.createOn;
    }

    public Task createOn(ZonedDateTime createOn) {
        this.createOn = createOn;
        return this;
    }

    public void setCreateOn(ZonedDateTime createOn) {
        this.createOn = createOn;
    }

    public ZonedDateTime getUpdatedOn() {
        return this.updatedOn;
    }

    public Task updatedOn(ZonedDateTime updatedOn) {
        this.updatedOn = updatedOn;
        return this;
    }

    public void setUpdatedOn(ZonedDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Persons getAssignTo() {
        return this.assignTo;
    }

    public Task assignTo(Persons persons) {
        this.setAssignTo(persons);
        return this;
    }

    public void setAssignTo(Persons persons) {
        this.assignTo = persons;
    }

    public Departments getDepartment() {
        return this.department;
    }

    public Task department(Departments departments) {
        this.setDepartment(departments);
        return this;
    }

    public void setDepartment(Departments departments) {
        this.department = departments;
    }

    public Area getArea() {
        return this.area;
    }

    public Task area(Area area) {
        this.setArea(area);
        return this;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public TaskType getType() {
        return this.type;
    }

    public Task type(TaskType taskType) {
        this.setType(taskType);
        return this;
    }

    public void setType(TaskType taskType) {
        this.type = taskType;
    }

    public Set<WorkNotes> getWorkNotes() {
        return this.workNotes;
    }

    public Task workNotes(Set<WorkNotes> workNotes) {
        this.setWorkNotes(workNotes);
        return this;
    }

    public Task addWorkNotes(WorkNotes workNotes) {
        this.workNotes.add(workNotes);
        workNotes.setTask(this);
        return this;
    }

    public Task removeWorkNotes(WorkNotes workNotes) {
        this.workNotes.remove(workNotes);
        workNotes.setTask(null);
        return this;
    }

    public void setWorkNotes(Set<WorkNotes> workNotes) {
        if (this.workNotes != null) {
            this.workNotes.forEach(i -> i.setTask(null));
        }
        if (workNotes != null) {
            workNotes.forEach(i -> i.setTask(this));
        }
        this.workNotes = workNotes;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Task)) {
            return false;
        }
        return id != null && id.equals(((Task) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Task{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            ", estimatedTimeToComplete=" + getEstimatedTimeToComplete() +
            ", estimatedTimeToCompleteTimeUnit='" + getEstimatedTimeToCompleteTimeUnit() + "'" +
            ", isReadByAssignTo='" + getIsReadByAssignTo() + "'" +
            ", isUrgent='" + getIsUrgent() + "'" +
            ", isRejected='" + getIsRejected() + "'" +
            ", isCompleted='" + getIsCompleted() + "'" +
            ", completedOn='" + getCompletedOn() + "'" +
            ", rejectedOn='" + getRejectedOn() + "'" +
            ", createOn='" + getCreateOn() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            "}";
    }
}
