package microapp.ticket.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import microapp.ticket.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TicketPriorityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TicketPriorityDTO.class);
        TicketPriorityDTO ticketPriorityDTO1 = new TicketPriorityDTO();
        ticketPriorityDTO1.setId(1L);
        TicketPriorityDTO ticketPriorityDTO2 = new TicketPriorityDTO();
        assertThat(ticketPriorityDTO1).isNotEqualTo(ticketPriorityDTO2);
        ticketPriorityDTO2.setId(ticketPriorityDTO1.getId());
        assertThat(ticketPriorityDTO1).isEqualTo(ticketPriorityDTO2);
        ticketPriorityDTO2.setId(2L);
        assertThat(ticketPriorityDTO1).isNotEqualTo(ticketPriorityDTO2);
        ticketPriorityDTO1.setId(null);
        assertThat(ticketPriorityDTO1).isNotEqualTo(ticketPriorityDTO2);
    }
}
