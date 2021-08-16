import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IPersons } from 'app/shared/model/persons.model';
import { getEntities as getPersons } from 'app/entities/persons/persons.reducer';
import { ITask } from 'app/shared/model/task.model';
import { getEntities as getTasks } from 'app/entities/task/task.reducer';
import { getEntity, updateEntity, createEntity, reset } from './work-notes.reducer';
import { IWorkNotes } from 'app/shared/model/work-notes.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WorkNotesUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const persons = useAppSelector(state => state.persons.entities);
  const tasks = useAppSelector(state => state.task.entities);
  const workNotesEntity = useAppSelector(state => state.workNotes.entity);
  const loading = useAppSelector(state => state.workNotes.loading);
  const updating = useAppSelector(state => state.workNotes.updating);
  const updateSuccess = useAppSelector(state => state.workNotes.updateSuccess);

  const handleClose = () => {
    props.history.push('/work-notes');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getPersons({}));
    dispatch(getTasks({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.createOn = convertDateTimeToServer(values.createOn);

    const entity = {
      ...workNotesEntity,
      ...values,
      createBy: persons.find(it => it.id.toString() === values.createById.toString()),
      task: tasks.find(it => it.id.toString() === values.taskId.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createOn: displayDefaultDateTime(),
        }
      : {
          ...workNotesEntity,
          createOn: convertDateTimeFromServer(workNotesEntity.createOn),
          createById: workNotesEntity?.createBy?.id,
          taskId: workNotesEntity?.task?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="taskManagerApp.workNotes.home.createOrEditLabel" data-cy="WorkNotesCreateUpdateHeading">
            <Translate contentKey="taskManagerApp.workNotes.home.createOrEditLabel">Create or edit a WorkNotes</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="work-notes-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('taskManagerApp.workNotes.text')}
                id="work-notes-text"
                name="text"
                data-cy="text"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('taskManagerApp.workNotes.createOn')}
                id="work-notes-createOn"
                name="createOn"
                data-cy="createOn"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="work-notes-createBy"
                name="createById"
                data-cy="createBy"
                label={translate('taskManagerApp.workNotes.createBy')}
                type="select"
              >
                <option value="" key="0" />
                {persons
                  ? persons.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="work-notes-task"
                name="taskId"
                data-cy="task"
                label={translate('taskManagerApp.workNotes.task')}
                type="select"
              >
                <option value="" key="0" />
                {tasks
                  ? tasks.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/work-notes" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default WorkNotesUpdate;
