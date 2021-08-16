import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Task from './task';
import WorkNotes from './work-notes';
import Persons from './persons';
import Departments from './departments';
import Area from './area';
import TaskType from './task-type';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}task`} component={Task} />
      <ErrorBoundaryRoute path={`${match.url}work-notes`} component={WorkNotes} />
      <ErrorBoundaryRoute path={`${match.url}persons`} component={Persons} />
      <ErrorBoundaryRoute path={`${match.url}departments`} component={Departments} />
      <ErrorBoundaryRoute path={`${match.url}area`} component={Area} />
      <ErrorBoundaryRoute path={`${match.url}task-type`} component={TaskType} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
