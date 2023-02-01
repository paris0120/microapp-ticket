import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TicketType from './ticket-type';
import TicketTypeDetail from './ticket-type-detail';
import TicketTypeUpdate from './ticket-type-update';
import TicketTypeDeleteDialog from './ticket-type-delete-dialog';

const TicketTypeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TicketType />} />
    <Route path="new" element={<TicketTypeUpdate />} />
    <Route path=":id">
      <Route index element={<TicketTypeDetail />} />
      <Route path="edit" element={<TicketTypeUpdate />} />
      <Route path="delete" element={<TicketTypeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TicketTypeRoutes;
