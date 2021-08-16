import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './work-notes.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WorkNotesDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const workNotesEntity = useAppSelector(state => state.workNotes.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="workNotesDetailsHeading">
          <Translate contentKey="taskManagerApp.workNotes.detail.title">WorkNotes</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{workNotesEntity.id}</dd>
          <dt>
            <span id="text">
              <Translate contentKey="taskManagerApp.workNotes.text">Text</Translate>
            </span>
          </dt>
          <dd>{workNotesEntity.text}</dd>
          <dt>
            <span id="createOn">
              <Translate contentKey="taskManagerApp.workNotes.createOn">Create On</Translate>
            </span>
          </dt>
          <dd>{workNotesEntity.createOn ? <TextFormat value={workNotesEntity.createOn} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="taskManagerApp.workNotes.createBy">Create By</Translate>
          </dt>
          <dd>{workNotesEntity.createBy ? workNotesEntity.createBy.name : ''}</dd>
          <dt>
            <Translate contentKey="taskManagerApp.workNotes.task">Task</Translate>
          </dt>
          <dd>{workNotesEntity.task ? workNotesEntity.task.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/work-notes" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/work-notes/${workNotesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default WorkNotesDetail;
