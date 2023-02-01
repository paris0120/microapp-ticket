package microapp.ticket.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TicketAssignmentMapperTest {

    private TicketAssignmentMapper ticketAssignmentMapper;

    @BeforeEach
    public void setUp() {
        ticketAssignmentMapper = new TicketAssignmentMapperImpl();
    }
}
