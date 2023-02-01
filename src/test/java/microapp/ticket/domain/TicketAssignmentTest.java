package microapp.ticket.domain;

import static org.assertj.core.api.Assertions.assertThat;

import microapp.ticket.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TicketAssignmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TicketAssignment.class);
        TicketAssignment ticketAssignment1 = new TicketAssignment();
        ticketAssignment1.setId(1L);
        TicketAssignment ticketAssignment2 = new TicketAssignment();
        ticketAssignment2.setId(ticketAssignment1.getId());
        assertThat(ticketAssignment1).isEqualTo(ticketAssignment2);
        ticketAssignment2.setId(2L);
        assertThat(ticketAssignment1).isNotEqualTo(ticketAssignment2);
        ticketAssignment1.setId(null);
        assertThat(ticketAssignment1).isNotEqualTo(ticketAssignment2);
    }
}
