package microapp.ticket.domain;

import static org.assertj.core.api.Assertions.assertThat;

import microapp.ticket.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TicketPriorityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TicketPriority.class);
        TicketPriority ticketPriority1 = new TicketPriority();
        ticketPriority1.setId(1L);
        TicketPriority ticketPriority2 = new TicketPriority();
        ticketPriority2.setId(ticketPriority1.getId());
        assertThat(ticketPriority1).isEqualTo(ticketPriority2);
        ticketPriority2.setId(2L);
        assertThat(ticketPriority1).isNotEqualTo(ticketPriority2);
        ticketPriority1.setId(null);
        assertThat(ticketPriority1).isNotEqualTo(ticketPriority2);
    }
}
