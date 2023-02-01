import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import { ReducersMapObject, combineReducers } from '@reduxjs/toolkit';

import getStore from 'app/config/store';

import entitiesReducers from './reducers';

import Ticket from './ticket/ticket';
import TicketType from './ticket/ticket-type';
import TicketAssignment from './ticket/ticket-assignment';
import TicketPriority from './ticket/ticket-priority';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  const store = getStore();
  store.injectReducer('ticket', combineReducers(entitiesReducers as ReducersMapObject));
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="/ticket/*" element={<Ticket />} />
        <Route path="/ticket-type/*" element={<TicketType />} />
        <Route path="/ticket-assignment/*" element={<TicketAssignment />} />
        <Route path="/ticket-priority/*" element={<TicketPriority />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
