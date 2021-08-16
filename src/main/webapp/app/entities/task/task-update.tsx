import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IPersons } from 'app/shared/model/persons.model';
import { getEntities as getPersons } from 'app/entities/persons/persons.reducer';
import { IDepartments } from 'app/shared/model/departments.model';
import { getEntities as getDepartments } from 'app/entities/departments/departments.reducer';
import { IArea } from 'app/shared/model/area.model';
import { getEntities as getAreas } from 'app/entities/area/area.reducer';
import { ITaskType } from 'app/shared/model/task-type.model';
import { getEntities as getTaskTypes } from 'app/entities/task-type/task-type.reducer';
import { getEntity, updateEntity, createEntity, reset } from './task.reducer';
import { ITask } from 'app/shared/model/task.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TaskUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const persons = useAppSelector(state => state.persons.entities);
  const departments = useAppSelector(state => state.departments.entities);
  const areas = useAppSelector(state => state.area.entities);
  const taskTypes = useAppSelector(state => state.taskType.entities);
  const taskEntity = useAppSelector(state => state.task.entity);
  const loading = useAppSelector(state => state.task.loading);
  const updating = useAppSelector(state => state.task.updating);
  const updateSuccess = useAppSelector(state => state.task.updateSuccess);

  const handleClose = () => {
    props.history.push('/task' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getPersons({}));
    dispatch(getDepartments({}));
    dispatch(getAreas({}));
    dispatch(getTaskTypes({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.dueDate = convertDateTimeToServer(values.dueDate);
    values.completedOn = convertDateTimeToServer(values.completedOn);
    values.rejectedOn = convertDateTimeToServer(values.rejectedOn);
    values.createOn = convertDateTimeToServer(values.createOn);
    values.updatedOn = convertDateTimeToServer(values.updatedOn);

    const entity = {
      ...taskEntity,
      ...values,
      assignTo: persons.find(it => it.id.toString() === values.assignToId.toString()),
      department: departments.find(it => it.id.toString() === values.departmentId.toString()),
      area: areas.find(it => it.id.toString() === values.areaId.toString()),
      type: taskTypes.find(it => it.id.toString() === values.typeId.toString()),
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
          dueDate: displayDefaultDateTime(),
          completedOn: displayDefaultDateTime(),
          rejectedOn: displayDefaultDateTime(),
          createOn: displayDefaultDateTime(),
          updatedOn: displayDefaultDateTime(),
        }
      : {
          ...taskEntity,
          dueDate: convertDateTimeFromServer(taskEntity.dueDate),
          completedOn: convertDateTimeFromServer(taskEntity.completedOn),
          rejectedOn: convertDateTimeFromServer(taskEntity.rejectedOn),
          createOn: convertDateTimeFromServer(taskEntity.createOn),
          updatedOn: convertDateTimeFromServer(taskEntity.updatedOn),
          assignToId: taskEntity?.assignTo?.id,
          departmentId: taskEntity?.department?.id,
          areaId: taskEntity?.area?.id,
          typeId: taskEntity?.type?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="taskManagerApp.task.home.createOrEditLabel" data-cy="TaskCreateUpdateHeading">
            <Translate contentKey="taskManagerApp.task.home.createOrEditLabel">Create or edit a Task</Translate>
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
                  id="task-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('taskManagerApp.task.description')}
                id="task-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('taskManagerApp.task.dueDate')}
                id="task-dueDate"
                name="dueDate"
                data-cy="dueDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('taskManagerApp.task.estimatedTimeToComplete')}
                id="task-estimatedTimeToComplete"
                name="estimatedTimeToComplete"
                data-cy="estimatedTimeToComplete"
                type="text"
              />
              <ValidatedField
                label={translate('taskManagerApp.task.estimatedTimeToCompleteTimeUnit')}
                id="task-estimatedTimeToCompleteTimeUnit"
                name="estimatedTimeToCompleteTimeUnit"
                data-cy="estimatedTimeToCompleteTimeUnit"
                type="text"
              />
              <ValidatedField
                label={translate('taskManagerApp.task.isReadByAssignTo')}
                id="task-isReadByAssignTo"
                name="isReadByAssignTo"
                data-cy="isReadByAssignTo"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('taskManagerApp.task.isUrgent')}
                id="task-isUrgent"
                name="isUrgent"
                data-cy="isUrgent"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('taskManagerApp.task.isRejected')}
                id="task-isRejected"
                name="isRejected"
                data-cy="isRejected"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('taskManagerApp.task.isCompleted')}
                id="task-isCompleted"
                name="isCompleted"
                data-cy="isCompleted"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('taskManagerApp.task.completedOn')}
                id="task-completedOn"
                name="completedOn"
                data-cy="completedOn"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('taskManagerApp.task.rejectedOn')}
                id="task-rejectedOn"
                name="rejectedOn"
                data-cy="rejectedOn"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('taskManagerApp.task.createOn')}
                id="task-createOn"
                name="createOn"
                data-cy="createOn"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('taskManagerApp.task.updatedOn')}
                id="task-updatedOn"
                name="updatedOn"
                data-cy="updatedOn"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="task-assignTo"
                name="assignToId"
                data-cy="assignTo"
                label={translate('taskManagerApp.task.assignTo')}
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
                id="task-department"
                name="departmentId"
                data-cy="department"
                label={translate('taskManagerApp.task.department')}
                type="select"
              >
                <option value="" key="0" />
                {departments
                  ? departments.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="task-area" name="areaId" data-cy="area" label={translate('taskManagerApp.task.area')} type="select">
                <option value="" key="0" />
                {areas
                  ? areas.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="task-type" name="typeId" data-cy="type" label={translate('taskManagerApp.task.type')} type="select">
                <option value="" key="0" />
                {taskTypes
                  ? taskTypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/task" replace color="info">
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

export default TaskUpdate;
