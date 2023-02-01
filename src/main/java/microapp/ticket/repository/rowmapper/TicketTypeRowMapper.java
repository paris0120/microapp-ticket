package microapp.ticket.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import microapp.ticket.domain.TicketType;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link TicketType}, with proper type conversions.
 */
@Service
public class TicketTypeRowMapper implements BiFunction<Row, String, TicketType> {

    private final ColumnConverter converter;

    public TicketTypeRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link TicketType} stored in the database.
     */
    @Override
    public TicketType apply(Row row, String prefix) {
        TicketType entity = new TicketType();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setKey(converter.fromRow(row, prefix + "_microapp_key", String.class));
        entity.setType(converter.fromRow(row, prefix + "_type", String.class));
        entity.setWeight(converter.fromRow(row, prefix + "_weight", Integer.class));
        entity.setColor(converter.fromRow(row, prefix + "_color", String.class));
        entity.setIcon(converter.fromRow(row, prefix + "_icon", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setParentType(converter.fromRow(row, prefix + "_parent_type", String.class));
        entity.setIsActive(converter.fromRow(row, prefix + "_is_active", Boolean.class));
        entity.setCreated(converter.fromRow(row, prefix + "_created", Instant.class));
        entity.setModified(converter.fromRow(row, prefix + "_modified", Instant.class));
        entity.setArchived(converter.fromRow(row, prefix + "_archived", Instant.class));
        return entity;
    }
}
