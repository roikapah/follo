import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import WorkNotes from './work-notes';
import WorkNotesDetail from './work-notes-detail';
import WorkNotesUpdate from './work-notes-update';
import WorkNotesDeleteDialog from './work-notes-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={WorkNotesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={WorkNotesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={WorkNotesDetail} />
      <ErrorBoundaryRoute path={match.url} component={WorkNotes} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={WorkNotesDeleteDialog} />
  </>
);

export default Routes;
