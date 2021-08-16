import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './task.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TaskDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const taskEntity = useAppSelector(state => state.task.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="taskDetailsHeading">
          <Translate contentKey="taskManagerApp.task.detail.title">Task</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{taskEntity.id}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="taskManagerApp.task.description">Description</Translate>
            </span>
          </dt>
          <dd>{taskEntity.description}</dd>
          <dt>
            <span id="dueDate">
              <Translate contentKey="taskManagerApp.task.dueDate">Due Date</Translate>
            </span>
          </dt>
          <dd>{taskEntity.dueDate ? <TextFormat value={taskEntity.dueDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="estimatedTimeToComplete">
              <Translate contentKey="taskManagerApp.task.estimatedTimeToComplete">Estimated Time To Complete</Translate>
            </span>
          </dt>
          <dd>{taskEntity.estimatedTimeToComplete}</dd>
          <dt>
            <span id="estimatedTimeToCompleteTimeUnit">
              <Translate contentKey="taskManagerApp.task.estimatedTimeToCompleteTimeUnit">Estimated Time To Complete Time Unit</Translate>
            </span>
          </dt>
          <dd>{taskEntity.estimatedTimeToCompleteTimeUnit}</dd>
          <dt>
            <span id="isReadByAssignTo">
              <Translate contentKey="taskManagerApp.task.isReadByAssignTo">Is Read By Assign To</Translate>
            </span>
          </dt>
          <dd>{taskEntity.isReadByAssignTo ? 'true' : 'false'}</dd>
          <dt>
            <span id="isUrgent">
              <Translate contentKey="taskManagerApp.task.isUrgent">Is Urgent</Translate>
            </span>
          </dt>
          <dd>{taskEntity.isUrgent ? 'true' : 'false'}</dd>
          <dt>
            <span id="isRejected">
              <Translate contentKey="taskManagerApp.task.isRejected">Is Rejected</Translate>
            </span>
          </dt>
          <dd>{taskEntity.isRejected ? 'true' : 'false'}</dd>
          <dt>
            <span id="isCompleted">
              <Translate contentKey="taskManagerApp.task.isCompleted">Is Completed</Translate>
            </span>
          </dt>
          <dd>{taskEntity.isCompleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="completedOn">
              <Translate contentKey="taskManagerApp.task.completedOn">Completed On</Translate>
            </span>
          </dt>
          <dd>{taskEntity.completedOn ? <TextFormat value={taskEntity.completedOn} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="rejectedOn">
              <Translate contentKey="taskManagerApp.task.rejectedOn">Rejected On</Translate>
            </span>
          </dt>
          <dd>{taskEntity.rejectedOn ? <TextFormat value={taskEntity.rejectedOn} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="createOn">
              <Translate contentKey="taskManagerApp.task.createOn">Create On</Translate>
            </span>
          </dt>
          <dd>{taskEntity.createOn ? <TextFormat value={taskEntity.createOn} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedOn">
              <Translate contentKey="taskManagerApp.task.updatedOn">Updated On</Translate>
            </span>
          </dt>
          <dd>{taskEntity.updatedOn ? <TextFormat value={taskEntity.updatedOn} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="taskManagerApp.task.assignTo">Assign To</Translate>
          </dt>
          <dd>{taskEntity.assignTo ? taskEntity.assignTo.name : ''}</dd>
          <dt>
            <Translate contentKey="taskManagerApp.task.department">Department</Translate>
          </dt>
          <dd>{taskEntity.department ? taskEntity.department.name : ''}</dd>
          <dt>
            <Translate contentKey="taskManagerApp.task.area">Area</Translate>
          </dt>
          <dd>{taskEntity.area ? taskEntity.area.name : ''}</dd>
          <dt>
            <Translate contentKey="taskManagerApp.task.type">Type</Translate>
          </dt>
          <dd>{taskEntity.type ? taskEntity.type.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/task" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/task/${taskEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TaskDetail;
