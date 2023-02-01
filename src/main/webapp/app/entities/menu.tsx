import React, { useEffect, useState } from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';
import { addTranslationSourcePrefix } from 'app/shared/reducers/locale';
import { useAppDispatch, useAppSelector } from 'app/config/store';

const EntitiesMenu = () => {
  const lastChange = useAppSelector(state => state.locale.lastChange);
  const dispatch = useAppDispatch();
  useEffect(() => {
    dispatch(addTranslationSourcePrefix('services/ticket/'));
  }, [lastChange]);

  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/ticket/ticket">
        <Translate contentKey="global.menu.entities.ticketTicket" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/ticket/ticket-type">
        <Translate contentKey="global.menu.entities.ticketTicketType" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/ticket/ticket-assignment">
        <Translate contentKey="global.menu.entities.ticketTicketAssignment" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/ticket/ticket-priority">
        <Translate contentKey="global.menu.entities.ticketTicketPriority" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
