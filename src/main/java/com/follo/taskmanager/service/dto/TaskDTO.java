package com.follo.taskmanager.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.follo.taskmanager.domain.Task} entity.
 */
public class TaskDTO implements Serializable {

    private Long id;

    private String description;

    private ZonedDateTime dueDate;

    private Integer estimatedTimeToComplete;

    private String estimatedTimeToCompleteTimeUnit;

    private Boolean isReadByAssignTo;

    private Boolean isUrgent;

    private Boolean isRejected;

    private Boolean isCompleted;

    private ZonedDateTime completedOn;

    private ZonedDateTime rejectedOn;

    private ZonedDateTime createOn;

    private ZonedDateTime updatedOn;

    private PersonsDTO assignTo;

    private DepartmentsDTO department;

    private AreaDTO area;

    private TaskTypeDTO type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(ZonedDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getEstimatedTimeToComplete() {
        return estimatedTimeToComplete;
    }

    public void setEstimatedTimeToComplete(Integer estimatedTimeToComplete) {
        this.estimatedTimeToComplete = estimatedTimeToComplete;
    }

    public String getEstimatedTimeToCompleteTimeUnit() {
        return estimatedTimeToCompleteTimeUnit;
    }

    public void setEstimatedTimeToCompleteTimeUnit(String estimatedTimeToCompleteTimeUnit) {
        this.estimatedTimeToCompleteTimeUnit = estimatedTimeToCompleteTimeUnit;
    }

    public Boolean getIsReadByAssignTo() {
        return isReadByAssignTo;
    }

    public void setIsReadByAssignTo(Boolean isReadByAssignTo) {
        this.isReadByAssignTo = isReadByAssignTo;
    }

    public Boolean getIsUrgent() {
        return isUrgent;
    }

    public void setIsUrgent(Boolean isUrgent) {
        this.isUrgent = isUrgent;
    }

    public Boolean getIsRejected() {
        return isRejected;
    }

    public void setIsRejected(Boolean isRejected) {
        this.isRejected = isRejected;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public ZonedDateTime getCompletedOn() {
        return completedOn;
    }

    public void setCompletedOn(ZonedDateTime completedOn) {
        this.completedOn = completedOn;
    }

    public ZonedDateTime getRejectedOn() {
        return rejectedOn;
    }

    public void setRejectedOn(ZonedDateTime rejectedOn) {
        this.rejectedOn = rejectedOn;
    }

    public ZonedDateTime getCreateOn() {
        return createOn;
    }

    public void setCreateOn(ZonedDateTime createOn) {
        this.createOn = createOn;
    }

    public ZonedDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(ZonedDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    public PersonsDTO getAssignTo() {
        return assignTo;
    }

    public void setAssignTo(PersonsDTO assignTo) {
        this.assignTo = assignTo;
    }

    public DepartmentsDTO getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentsDTO department) {
        this.department = department;
    }

    public AreaDTO getArea() {
        return area;
    }

    public void setArea(AreaDTO area) {
        this.area = area;
    }

    public TaskTypeDTO getType() {
        return type;
    }

    public void setType(TaskTypeDTO type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaskDTO)) {
            return false;
        }

        TaskDTO taskDTO = (TaskDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, taskDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaskDTO{" +
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
            ", assignTo=" + getAssignTo() +
            ", department=" + getDepartment() +
            ", area=" + getArea() +
            ", type=" + getType() +
            "}";
    }
}
