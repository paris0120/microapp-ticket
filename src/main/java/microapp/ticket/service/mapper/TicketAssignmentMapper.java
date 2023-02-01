package microapp.ticket.service.mapper;

import microapp.ticket.domain.TicketAssignment;
import microapp.ticket.service.dto.TicketAssignmentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TicketAssignment} and its DTO {@link TicketAssignmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface TicketAssignmentMapper extends EntityMapper<TicketAssignmentDTO, TicketAssignment> {}
