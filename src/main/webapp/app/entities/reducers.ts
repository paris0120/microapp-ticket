import ticket from 'app/entities/ticket/ticket/ticket.reducer';
import ticketType from 'app/entities/ticket/ticket-type/ticket-type.reducer';
import ticketAssignment from 'app/entities/ticket/ticket-assignment/ticket-assignment.reducer';
import ticketPriority from 'app/entities/ticket/ticket-priority/ticket-priority.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  ticket,
  ticketType,
  ticketAssignment,
  ticketPriority,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
