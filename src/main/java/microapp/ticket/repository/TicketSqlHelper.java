package microapp.ticket.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class TicketSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("username", table, columnPrefix + "_username"));
        columns.add(Column.aliased("user_first_name", table, columnPrefix + "_user_first_name"));
        columns.add(Column.aliased("user_last_name", table, columnPrefix + "_user_last_name"));
        columns.add(Column.aliased("user_display_name", table, columnPrefix + "_user_display_name"));
        columns.add(Column.aliased("title", table, columnPrefix + "_title"));
        columns.add(Column.aliased("content", table, columnPrefix + "_content"));
        columns.add(Column.aliased("type_key", table, columnPrefix + "_type_key"));
        columns.add(Column.aliased("workflow_status_key", table, columnPrefix + "_workflow_status_key"));
        columns.add(Column.aliased("priority_level", table, columnPrefix + "_priority_level"));
        columns.add(Column.aliased("tags", table, columnPrefix + "_tags"));
        columns.add(Column.aliased("total_comments", table, columnPrefix + "_total_comments"));
        columns.add(Column.aliased("uuid", table, columnPrefix + "_uuid"));
        columns.add(Column.aliased("created", table, columnPrefix + "_created"));
        columns.add(Column.aliased("modified", table, columnPrefix + "_modified"));
        columns.add(Column.aliased("updated", table, columnPrefix + "_updated"));
        columns.add(Column.aliased("closed", table, columnPrefix + "_closed"));
        columns.add(Column.aliased("archived", table, columnPrefix + "_archived"));

        return columns;
    }
}
