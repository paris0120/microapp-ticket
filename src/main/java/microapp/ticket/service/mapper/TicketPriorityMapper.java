package microapp.ticket.service.mapper;

import microapp.ticket.domain.TicketPriority;
import microapp.ticket.service.dto.TicketPriorityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TicketPriority} and its DTO {@link TicketPriorityDTO}.
 */
@Mapper(componentModel = "spring")
public interface TicketPriorityMapper extends EntityMapper<TicketPriorityDTO, TicketPriority> {}
