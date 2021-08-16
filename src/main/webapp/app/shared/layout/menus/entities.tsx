import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { Translate, translate } from 'react-jhipster';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown
    icon="th-list"
    name={translate('global.menu.entities.main')}
    id="entity-menu"
    data-cy="entity"
    style={{ maxHeight: '80vh', overflow: 'auto' }}
  >
    <>{/* to avoid warnings when empty */}</>
    <MenuItem icon="asterisk" to="/task">
      <Translate contentKey="global.menu.entities.task" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/work-notes">
      <Translate contentKey="global.menu.entities.workNotes" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/persons">
      <Translate contentKey="global.menu.entities.persons" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/departments">
      <Translate contentKey="global.menu.entities.departments" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/area">
      <Translate contentKey="global.menu.entities.area" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/task-type">
      <Translate contentKey="global.menu.entities.taskType" />
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
