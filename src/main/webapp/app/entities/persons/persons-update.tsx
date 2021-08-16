import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IDepartments } from 'app/shared/model/departments.model';
import { getEntities as getDepartments } from 'app/entities/departments/departments.reducer';
import { getEntity, updateEntity, createEntity, reset } from './persons.reducer';
import { IPersons } from 'app/shared/model/persons.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const PersonsUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const departments = useAppSelector(state => state.departments.entities);
  const personsEntity = useAppSelector(state => state.persons.entity);
  const loading = useAppSelector(state => state.persons.loading);
  const updating = useAppSelector(state => state.persons.updating);
  const updateSuccess = useAppSelector(state => state.persons.updateSuccess);

  const handleClose = () => {
    props.history.push('/persons' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getDepartments({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.createOn = convertDateTimeToServer(values.createOn);
    values.updatedOn = convertDateTimeToServer(values.updatedOn);

    const entity = {
      ...personsEntity,
      ...values,
      department: departments.find(it => it.id.toString() === values.departmentId.toString()),
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
          updatedOn: displayDefaultDateTime(),
        }
      : {
          ...personsEntity,
          role: 'WORKER',
          createOn: convertDateTimeFromServer(personsEntity.createOn),
          updatedOn: convertDateTimeFromServer(personsEntity.updatedOn),
          departmentId: personsEntity?.department?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="taskManagerApp.persons.home.createOrEditLabel" data-cy="PersonsCreateUpdateHeading">
            <Translate contentKey="taskManagerApp.persons.home.createOrEditLabel">Create or edit a Persons</Translate>
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
                  id="persons-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('taskManagerApp.persons.name')} id="persons-name" name="name" data-cy="name" type="text" />
              <ValidatedField
                label={translate('taskManagerApp.persons.email')}
                id="persons-email"
                name="email"
                data-cy="email"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField label={translate('taskManagerApp.persons.role')} id="persons-role" name="role" data-cy="role" type="select">
                <option value="WORKER">{translate('taskManagerApp.Role.WORKER')}</option>
                <option value="MANAGER">{translate('taskManagerApp.Role.MANAGER')}</option>
                <option value="ADMIN">{translate('taskManagerApp.Role.ADMIN')}</option>
              </ValidatedField>
              <ValidatedField
                label={translate('taskManagerApp.persons.phoneNumber')}
                id="persons-phoneNumber"
                name="phoneNumber"
                data-cy="phoneNumber"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('taskManagerApp.persons.address')}
                id="persons-address"
                name="address"
                data-cy="address"
                type="text"
              />
              <ValidatedField
                label={translate('taskManagerApp.persons.createOn')}
                id="persons-createOn"
                name="createOn"
                data-cy="createOn"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('taskManagerApp.persons.updatedOn')}
                id="persons-updatedOn"
                name="updatedOn"
                data-cy="updatedOn"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="persons-department"
                name="departmentId"
                data-cy="department"
                label={translate('taskManagerApp.persons.department')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/persons" replace color="info">
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

export default PersonsUpdate;
