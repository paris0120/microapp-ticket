package microapp.ticket.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.UUID;
import java.util.function.BiFunction;
import microapp.ticket.domain.Ticket;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Ticket}, with proper type conversions.
 */
@Service
public class TicketRowMapper implements BiFunction<Row, String, Ticket> {

    private final ColumnConverter converter;

    public TicketRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Ticket} stored in the database.
     */
    @Override
    public Ticket apply(Row row, String prefix) {
        Ticket entity = new Ticket();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setUsername(converter.fromRow(row, prefix + "_username", String.class));
        entity.setUserFirstName(converter.fromRow(row, prefix + "_user_first_name", String.class));
        entity.setUserLastName(converter.fromRow(row, prefix + "_user_last_name", String.class));
        entity.setUserDisplayName(converter.fromRow(row, prefix + "_user_display_name", String.class));
        entity.setTitle(converter.fromRow(row, prefix + "_title", String.class));
        entity.setContent(converter.fromRow(row, prefix + "_content", String.class));
        entity.setTypeKey(converter.fromRow(row, prefix + "_type_key", String.class));
        entity.setWorkflowStatusKey(converter.fromRow(row, prefix + "_workflow_status_key", String.class));
        entity.setPriorityLevel(converter.fromRow(row, prefix + "_priority_level", Integer.class));
        entity.setTags(converter.fromRow(row, prefix + "_tags", String.class));
        entity.setTotalComments(converter.fromRow(row, prefix + "_total_comments", Integer.class));
        entity.setUuid(converter.fromRow(row, prefix + "_uuid", UUID.class));
        entity.setCreated(converter.fromRow(row, prefix + "_created", Instant.class));
        entity.setModified(converter.fromRow(row, prefix + "_modified", Instant.class));
        entity.setUpdated(converter.fromRow(row, prefix + "_updated", Instant.class));
        entity.setClosed(converter.fromRow(row, prefix + "_closed", Instant.class));
        entity.setArchived(converter.fromRow(row, prefix + "_archived", Instant.class));
        return entity;
    }
}
