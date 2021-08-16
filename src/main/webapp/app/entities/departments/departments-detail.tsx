import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './departments.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const DepartmentsDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const departmentsEntity = useAppSelector(state => state.departments.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="departmentsDetailsHeading">
          <Translate contentKey="taskManagerApp.departments.detail.title">Departments</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{departmentsEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="taskManagerApp.departments.name">Name</Translate>
            </span>
          </dt>
          <dd>{departmentsEntity.name}</dd>
          <dt>
            <span id="createOn">
              <Translate contentKey="taskManagerApp.departments.createOn">Create On</Translate>
            </span>
          </dt>
          <dd>
            {departmentsEntity.createOn ? <TextFormat value={departmentsEntity.createOn} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedOn">
              <Translate contentKey="taskManagerApp.departments.updatedOn">Updated On</Translate>
            </span>
          </dt>
          <dd>
            {departmentsEntity.updatedOn ? <TextFormat value={departmentsEntity.updatedOn} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
        </dl>
        <Button tag={Link} to="/departments" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/departments/${departmentsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DepartmentsDetail;
