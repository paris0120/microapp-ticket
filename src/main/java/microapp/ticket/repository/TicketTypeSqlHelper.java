package microapp.ticket.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class TicketTypeSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("microapp_key", table, columnPrefix + "_microapp_key"));
        columns.add(Column.aliased("type", table, columnPrefix + "_type"));
        columns.add(Column.aliased("weight", table, columnPrefix + "_weight"));
        columns.add(Column.aliased("color", table, columnPrefix + "_color"));
        columns.add(Column.aliased("icon", table, columnPrefix + "_icon"));
        columns.add(Column.aliased("description", table, columnPrefix + "_description"));
        columns.add(Column.aliased("parent_type", table, columnPrefix + "_parent_type"));
        columns.add(Column.aliased("is_active", table, columnPrefix + "_is_active"));
        columns.add(Column.aliased("created", table, columnPrefix + "_created"));
        columns.add(Column.aliased("modified", table, columnPrefix + "_modified"));
        columns.add(Column.aliased("archived", table, columnPrefix + "_archived"));

        return columns;
    }
}
