import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import TaskType from './task-type';
import TaskTypeDetail from './task-type-detail';
import TaskTypeUpdate from './task-type-update';
import TaskTypeDeleteDialog from './task-type-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TaskTypeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TaskTypeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TaskTypeDetail} />
      <ErrorBoundaryRoute path={match.url} component={TaskType} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={TaskTypeDeleteDialog} />
  </>
);

export default Routes;
