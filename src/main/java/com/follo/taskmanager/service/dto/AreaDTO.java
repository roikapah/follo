package com.follo.taskmanager.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.follo.taskmanager.domain.Area} entity.
 */
public class AreaDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private ZonedDateTime createOn;

    private ZonedDateTime updatedOn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AreaDTO)) {
            return false;
        }

        AreaDTO areaDTO = (AreaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, areaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AreaDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", createOn='" + getCreateOn() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            "}";
    }
}
