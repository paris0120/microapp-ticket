package microapp.ticket.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.UUID;
import java.util.function.BiFunction;
import microapp.ticket.domain.TicketAssignment;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link TicketAssignment}, with proper type conversions.
 */
@Service
public class TicketAssignmentRowMapper implements BiFunction<Row, String, TicketAssignment> {

    private final ColumnConverter converter;

    public TicketAssignmentRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link TicketAssignment} stored in the database.
     */
    @Override
    public TicketAssignment apply(Row row, String prefix) {
        TicketAssignment entity = new TicketAssignment();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setIssueId(converter.fromRow(row, prefix + "_issue_id", Long.class));
        entity.setIssueUuid(converter.fromRow(row, prefix + "_issue_uuid", UUID.class));
        entity.setUsername(converter.fromRow(row, prefix + "_username", String.class));
        entity.setRoleKey(converter.fromRow(row, prefix + "_role_key", String.class));
        entity.setRoleWeight(converter.fromRow(row, prefix + "_role_weight", Integer.class));
        entity.setDepartmentKey(converter.fromRow(row, prefix + "_department_key", String.class));
        entity.setDepartmentWeight(converter.fromRow(row, prefix + "_department_weight", Integer.class));
        entity.setCreated(converter.fromRow(row, prefix + "_created", Instant.class));
        entity.setModified(converter.fromRow(row, prefix + "_modified", Instant.class));
        entity.setAccepted(converter.fromRow(row, prefix + "_accepted", Instant.class));
        entity.setLeft(converter.fromRow(row, prefix + "_microapp_left", Instant.class));
        entity.setClosed(converter.fromRow(row, prefix + "_closed", Instant.class));
        entity.setArchived(converter.fromRow(row, prefix + "_archived", Instant.class));
        return entity;
    }
}
