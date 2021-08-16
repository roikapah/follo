package com.follo.taskmanager.service.dto;

import com.follo.taskmanager.domain.enumeration.Role;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.follo.taskmanager.domain.Persons} entity.
 */
public class PersonsDTO implements Serializable {

    private Long id;

    private String name;

    @NotNull
    private String email;

    @NotNull
    private Role role;

    @NotNull
    private String phoneNumber;

    private String address;

    private ZonedDateTime createOn;

    private ZonedDateTime updatedOn;

    private DepartmentsDTO department;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public DepartmentsDTO getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentsDTO department) {
        this.department = department;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonsDTO)) {
            return false;
        }

        PersonsDTO personsDTO = (PersonsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, personsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonsDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", role='" + getRole() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", address='" + getAddress() + "'" +
            ", createOn='" + getCreateOn() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", department=" + getDepartment() +
            "}";
    }
}
