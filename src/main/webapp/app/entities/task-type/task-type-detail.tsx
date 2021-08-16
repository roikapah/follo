import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './task-type.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TaskTypeDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const taskTypeEntity = useAppSelector(state => state.taskType.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="taskTypeDetailsHeading">
          <Translate contentKey="taskManagerApp.taskType.detail.title">TaskType</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{taskTypeEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="taskManagerApp.taskType.name">Name</Translate>
            </span>
          </dt>
          <dd>{taskTypeEntity.name}</dd>
          <dt>
            <span id="createOn">
              <Translate contentKey="taskManagerApp.taskType.createOn">Create On</Translate>
            </span>
          </dt>
          <dd>{taskTypeEntity.createOn ? <TextFormat value={taskTypeEntity.createOn} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedOn">
              <Translate contentKey="taskManagerApp.taskType.updatedOn">Updated On</Translate>
            </span>
          </dt>
          <dd>{taskTypeEntity.updatedOn ? <TextFormat value={taskTypeEntity.updatedOn} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/task-type" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/task-type/${taskTypeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TaskTypeDetail;
