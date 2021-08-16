import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './task-type.reducer';
import { ITaskType } from 'app/shared/model/task-type.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TaskType = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const taskTypeList = useAppSelector(state => state.taskType.entities);
  const loading = useAppSelector(state => state.taskType.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="task-type-heading" data-cy="TaskTypeHeading">
        <Translate contentKey="taskManagerApp.taskType.home.title">Task Types</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="taskManagerApp.taskType.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="taskManagerApp.taskType.home.createLabel">Create new Task Type</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {taskTypeList && taskTypeList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="taskManagerApp.taskType.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="taskManagerApp.taskType.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="taskManagerApp.taskType.createOn">Create On</Translate>
                </th>
                <th>
                  <Translate contentKey="taskManagerApp.taskType.updatedOn">Updated On</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {taskTypeList.map((taskType, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${taskType.id}`} color="link" size="sm">
                      {taskType.id}
                    </Button>
                  </td>
                  <td>{taskType.name}</td>
                  <td>{taskType.createOn ? <TextFormat type="date" value={taskType.createOn} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{taskType.updatedOn ? <TextFormat type="date" value={taskType.updatedOn} format={APP_DATE_FORMAT} /> : null}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${taskType.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${taskType.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${taskType.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="taskManagerApp.taskType.home.notFound">No Task Types found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default TaskType;
