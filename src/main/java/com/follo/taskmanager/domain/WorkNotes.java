package com.follo.taskmanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WorkNotes.
 */
@Entity
@Table(name = "work_notes")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WorkNotes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "text", nullable = false)
    private String text;

    @NotNull
    @Column(name = "create_on", nullable = false)
    private ZonedDateTime createOn;

    @JsonIgnoreProperties(value = { "department" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Persons createBy;

    @ManyToOne
    @JsonIgnoreProperties(value = { "assignTo", "department", "area", "type", "workNotes" }, allowSetters = true)
    private Task task;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkNotes id(Long id) {
        this.id = id;
        return this;
    }

    public String getText() {
        return this.text;
    }

    public WorkNotes text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ZonedDateTime getCreateOn() {
        return this.createOn;
    }

    public WorkNotes createOn(ZonedDateTime createOn) {
        this.createOn = createOn;
        return this;
    }

    public void setCreateOn(ZonedDateTime createOn) {
        this.createOn = createOn;
    }

    public Persons getCreateBy() {
        return this.createBy;
    }

    public WorkNotes createBy(Persons persons) {
        this.setCreateBy(persons);
        return this;
    }

    public void setCreateBy(Persons persons) {
        this.createBy = persons;
    }

    public Task getTask() {
        return this.task;
    }

    public WorkNotes task(Task task) {
        this.setTask(task);
        return this;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkNotes)) {
            return false;
        }
        return id != null && id.equals(((WorkNotes) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkNotes{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            ", createOn='" + getCreateOn() + "'" +
            "}";
    }
}
