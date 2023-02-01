import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TicketAssignment from './ticket-assignment';
import TicketAssignmentDetail from './ticket-assignment-detail';
import TicketAssignmentUpdate from './ticket-assignment-update';
import TicketAssignmentDeleteDialog from './ticket-assignment-delete-dialog';

const TicketAssignmentRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TicketAssignment />} />
    <Route path="new" element={<TicketAssignmentUpdate />} />
    <Route path=":id">
      <Route index element={<TicketAssignmentDetail />} />
      <Route path="edit" element={<TicketAssignmentUpdate />} />
      <Route path="delete" element={<TicketAssignmentDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TicketAssignmentRoutes;
