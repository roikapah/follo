import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Area from './area';
import AreaDetail from './area-detail';
import AreaUpdate from './area-update';
import AreaDeleteDialog from './area-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AreaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AreaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AreaDetail} />
      <ErrorBoundaryRoute path={match.url} component={Area} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AreaDeleteDialog} />
  </>
);

export default Routes;
