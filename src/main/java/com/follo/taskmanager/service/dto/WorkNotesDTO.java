package com.follo.taskmanager.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.follo.taskmanager.domain.WorkNotes} entity.
 */
public class WorkNotesDTO implements Serializable {

    private Long id;

    @NotNull
    private String text;

    @NotNull
    private ZonedDateTime createOn;

    private PersonsDTO createBy;

    private TaskDTO task;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ZonedDateTime getCreateOn() {
        return createOn;
    }

    public void setCreateOn(ZonedDateTime createOn) {
        this.createOn = createOn;
    }

    public PersonsDTO getCreateBy() {
        return createBy;
    }

    public void setCreateBy(PersonsDTO createBy) {
        this.createBy = createBy;
    }

    public TaskDTO getTask() {
        return task;
    }

    public void setTask(TaskDTO task) {
        this.task = task;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkNotesDTO)) {
            return false;
        }

        WorkNotesDTO workNotesDTO = (WorkNotesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, workNotesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkNotesDTO{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            ", createOn='" + getCreateOn() + "'" +
            ", createBy=" + getCreateBy() +
            ", task=" + getTask() +
            "}";
    }
}
