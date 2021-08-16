import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './departments.reducer';
import { IDepartments } from 'app/shared/model/departments.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Departments = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const departmentsList = useAppSelector(state => state.departments.entities);
  const loading = useAppSelector(state => state.departments.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="departments-heading" data-cy="DepartmentsHeading">
        <Translate contentKey="taskManagerApp.departments.home.title">Departments</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="taskManagerApp.departments.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="taskManagerApp.departments.home.createLabel">Create new Departments</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {departmentsList && departmentsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="taskManagerApp.departments.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="taskManagerApp.departments.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="taskManagerApp.departments.createOn">Create On</Translate>
                </th>
                <th>
                  <Translate contentKey="taskManagerApp.departments.updatedOn">Updated On</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {departmentsList.map((departments, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${departments.id}`} color="link" size="sm">
                      {departments.id}
                    </Button>
                  </td>
                  <td>{departments.name}</td>
                  <td>{departments.createOn ? <TextFormat type="date" value={departments.createOn} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>
                    {departments.updatedOn ? <TextFormat type="date" value={departments.updatedOn} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${departments.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${departments.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${departments.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="taskManagerApp.departments.home.notFound">No Departments found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Departments;
