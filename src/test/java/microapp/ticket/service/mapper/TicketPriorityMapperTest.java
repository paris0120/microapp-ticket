package microapp.ticket.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TicketPriorityMapperTest {

    private TicketPriorityMapper ticketPriorityMapper;

    @BeforeEach
    public void setUp() {
        ticketPriorityMapper = new TicketPriorityMapperImpl();
    }
}
