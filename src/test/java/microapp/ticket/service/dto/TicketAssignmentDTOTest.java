package microapp.ticket.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import microapp.ticket.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TicketAssignmentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TicketAssignmentDTO.class);
        TicketAssignmentDTO ticketAssignmentDTO1 = new TicketAssignmentDTO();
        ticketAssignmentDTO1.setId(1L);
        TicketAssignmentDTO ticketAssignmentDTO2 = new TicketAssignmentDTO();
        assertThat(ticketAssignmentDTO1).isNotEqualTo(ticketAssignmentDTO2);
        ticketAssignmentDTO2.setId(ticketAssignmentDTO1.getId());
        assertThat(ticketAssignmentDTO1).isEqualTo(ticketAssignmentDTO2);
        ticketAssignmentDTO2.setId(2L);
        assertThat(ticketAssignmentDTO1).isNotEqualTo(ticketAssignmentDTO2);
        ticketAssignmentDTO1.setId(null);
        assertThat(ticketAssignmentDTO1).isNotEqualTo(ticketAssignmentDTO2);
    }
}
