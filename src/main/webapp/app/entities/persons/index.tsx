import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Persons from './persons';
import PersonsDetail from './persons-detail';
import PersonsUpdate from './persons-update';
import PersonsDeleteDialog from './persons-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PersonsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PersonsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PersonsDetail} />
      <ErrorBoundaryRoute path={match.url} component={Persons} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PersonsDeleteDialog} />
  </>
);

export default Routes;
