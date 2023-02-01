package microapp.ticket.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TicketTypeMapperTest {

    private TicketTypeMapper ticketTypeMapper;

    @BeforeEach
    public void setUp() {
        ticketTypeMapper = new TicketTypeMapperImpl();
    }
}
