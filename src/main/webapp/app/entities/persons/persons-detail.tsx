import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './persons.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const PersonsDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const personsEntity = useAppSelector(state => state.persons.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="personsDetailsHeading">
          <Translate contentKey="taskManagerApp.persons.detail.title">Persons</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{personsEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="taskManagerApp.persons.name">Name</Translate>
            </span>
          </dt>
          <dd>{personsEntity.name}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="taskManagerApp.persons.email">Email</Translate>
            </span>
          </dt>
          <dd>{personsEntity.email}</dd>
          <dt>
            <span id="role">
              <Translate contentKey="taskManagerApp.persons.role">Role</Translate>
            </span>
          </dt>
          <dd>{personsEntity.role}</dd>
          <dt>
            <span id="phoneNumber">
              <Translate contentKey="taskManagerApp.persons.phoneNumber">Phone Number</Translate>
            </span>
          </dt>
          <dd>{personsEntity.phoneNumber}</dd>
          <dt>
            <span id="address">
              <Translate contentKey="taskManagerApp.persons.address">Address</Translate>
            </span>
          </dt>
          <dd>{personsEntity.address}</dd>
          <dt>
            <span id="createOn">
              <Translate contentKey="taskManagerApp.persons.createOn">Create On</Translate>
            </span>
          </dt>
          <dd>{personsEntity.createOn ? <TextFormat value={personsEntity.createOn} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedOn">
              <Translate contentKey="taskManagerApp.persons.updatedOn">Updated On</Translate>
            </span>
          </dt>
          <dd>{personsEntity.updatedOn ? <TextFormat value={personsEntity.updatedOn} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="taskManagerApp.persons.department">Department</Translate>
          </dt>
          <dd>{personsEntity.department ? personsEntity.department.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/persons" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/persons/${personsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PersonsDetail;
