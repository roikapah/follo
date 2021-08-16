import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './task.reducer';
import { ITask } from 'app/shared/model/task.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Task = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(props.location, ITEMS_PER_PAGE, 'id'), props.location.search)
  );

  const taskList = useAppSelector(state => state.task.entities);
  const loading = useAppSelector(state => state.task.loading);
  const totalItems = useAppSelector(state => state.task.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      })
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (props.location.search !== endURL) {
      props.history.push(`${props.location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(props.location.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [props.location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const { match } = props;

  return (
    <div>
      <h2 id="task-heading" data-cy="TaskHeading">
        <Translate contentKey="taskManagerApp.task.home.title">Tasks</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="taskManagerApp.task.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="taskManagerApp.task.home.createLabel">Create new Task</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {taskList && taskList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="taskManagerApp.task.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="taskManagerApp.task.description">Description</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('dueDate')}>
                  <Translate contentKey="taskManagerApp.task.dueDate">Due Date</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('estimatedTimeToComplete')}>
                  <Translate contentKey="taskManagerApp.task.estimatedTimeToComplete">Estimated Time To Complete</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('estimatedTimeToCompleteTimeUnit')}>
                  <Translate contentKey="taskManagerApp.task.estimatedTimeToCompleteTimeUnit">
                    Estimated Time To Complete Time Unit
                  </Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('isReadByAssignTo')}>
                  <Translate contentKey="taskManagerApp.task.isReadByAssignTo">Is Read By Assign To</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('isUrgent')}>
                  <Translate contentKey="taskManagerApp.task.isUrgent">Is Urgent</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('isRejected')}>
                  <Translate contentKey="taskManagerApp.task.isRejected">Is Rejected</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('isCompleted')}>
                  <Translate contentKey="taskManagerApp.task.isCompleted">Is Completed</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('completedOn')}>
                  <Translate contentKey="taskManagerApp.task.completedOn">Completed On</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('rejectedOn')}>
                  <Translate contentKey="taskManagerApp.task.rejectedOn">Rejected On</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('createOn')}>
                  <Translate contentKey="taskManagerApp.task.createOn">Create On</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('updatedOn')}>
                  <Translate contentKey="taskManagerApp.task.updatedOn">Updated On</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="taskManagerApp.task.assignTo">Assign To</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="taskManagerApp.task.department">Department</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="taskManagerApp.task.area">Area</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="taskManagerApp.task.type">Type</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {taskList.map((task, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${task.id}`} color="link" size="sm">
                      {task.id}
                    </Button>
                  </td>
                  <td>{task.description}</td>
                  <td>{task.dueDate ? <TextFormat type="date" value={task.dueDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{task.estimatedTimeToComplete}</td>
                  <td>{task.estimatedTimeToCompleteTimeUnit}</td>
                  <td>{task.isReadByAssignTo ? 'true' : 'false'}</td>
                  <td>{task.isUrgent ? 'true' : 'false'}</td>
                  <td>{task.isRejected ? 'true' : 'false'}</td>
                  <td>{task.isCompleted ? 'true' : 'false'}</td>
                  <td>{task.completedOn ? <TextFormat type="date" value={task.completedOn} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{task.rejectedOn ? <TextFormat type="date" value={task.rejectedOn} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{task.createOn ? <TextFormat type="date" value={task.createOn} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{task.updatedOn ? <TextFormat type="date" value={task.updatedOn} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{task.assignTo ? <Link to={`persons/${task.assignTo.id}`}>{task.assignTo.name}</Link> : ''}</td>
                  <td>{task.department ? <Link to={`departments/${task.department.id}`}>{task.department.name}</Link> : ''}</td>
                  <td>{task.area ? <Link to={`area/${task.area.id}`}>{task.area.name}</Link> : ''}</td>
                  <td>{task.type ? <Link to={`task-type/${task.type.id}`}>{task.type.name}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${task.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${task.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${task.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
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
              <Translate contentKey="taskManagerApp.task.home.notFound">No Tasks found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={taskList && taskList.length > 0 ? '' : 'd-none'}>
          <Row className="justify-content-center">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </Row>
          <Row className="justify-content-center">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </Row>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default Task;
