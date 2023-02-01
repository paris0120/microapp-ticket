package microapp.ticket.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class TicketPrioritySqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("priority_level", table, columnPrefix + "_priority_level"));
        columns.add(Column.aliased("priority", table, columnPrefix + "_priority"));
        columns.add(Column.aliased("color", table, columnPrefix + "_color"));
        columns.add(Column.aliased("icon", table, columnPrefix + "_icon"));

        return columns;
    }
}
