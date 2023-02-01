package microapp.ticket.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import microapp.ticket.domain.TicketPriority;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link TicketPriority}, with proper type conversions.
 */
@Service
public class TicketPriorityRowMapper implements BiFunction<Row, String, TicketPriority> {

    private final ColumnConverter converter;

    public TicketPriorityRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link TicketPriority} stored in the database.
     */
    @Override
    public TicketPriority apply(Row row, String prefix) {
        TicketPriority entity = new TicketPriority();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setPriorityLevel(converter.fromRow(row, prefix + "_priority_level", Integer.class));
        entity.setPriority(converter.fromRow(row, prefix + "_priority", String.class));
        entity.setColor(converter.fromRow(row, prefix + "_color", String.class));
        entity.setIcon(converter.fromRow(row, prefix + "_icon", String.class));
        return entity;
    }
}
