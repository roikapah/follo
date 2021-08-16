import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './work-notes.reducer';
import { IWorkNotes } from 'app/shared/model/work-notes.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WorkNotes = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const workNotesList = useAppSelector(state => state.workNotes.entities);
  const loading = useAppSelector(state => state.workNotes.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="work-notes-heading" data-cy="WorkNotesHeading">
        <Translate contentKey="taskManagerApp.workNotes.home.title">Work Notes</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="taskManagerApp.workNotes.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="taskManagerApp.workNotes.home.createLabel">Create new Work Notes</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {workNotesList && workNotesList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="taskManagerApp.workNotes.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="taskManagerApp.workNotes.text">Text</Translate>
                </th>
                <th>
                  <Translate contentKey="taskManagerApp.workNotes.createOn">Create On</Translate>
                </th>
                <th>
                  <Translate contentKey="taskManagerApp.workNotes.createBy">Create By</Translate>
                </th>
                <th>
                  <Translate contentKey="taskManagerApp.workNotes.task">Task</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {workNotesList.map((workNotes, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${workNotes.id}`} color="link" size="sm">
                      {workNotes.id}
                    </Button>
                  </td>
                  <td>{workNotes.text}</td>
                  <td>{workNotes.createOn ? <TextFormat type="date" value={workNotes.createOn} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{workNotes.createBy ? <Link to={`persons/${workNotes.createBy.id}`}>{workNotes.createBy.name}</Link> : ''}</td>
                  <td>{workNotes.task ? <Link to={`task/${workNotes.task.id}`}>{workNotes.task.id}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${workNotes.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${workNotes.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${workNotes.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="taskManagerApp.workNotes.home.notFound">No Work Notes found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default WorkNotes;
