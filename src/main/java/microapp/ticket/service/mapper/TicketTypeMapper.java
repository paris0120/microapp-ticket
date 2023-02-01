package microapp.ticket.service.mapper;

import microapp.ticket.domain.TicketType;
import microapp.ticket.service.dto.TicketTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TicketType} and its DTO {@link TicketTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface TicketTypeMapper extends EntityMapper<TicketTypeDTO, TicketType> {}
