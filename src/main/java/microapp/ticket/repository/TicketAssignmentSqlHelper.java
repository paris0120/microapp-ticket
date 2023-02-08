package microapp.ticket.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class TicketAssignmentSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("issue_id", table, columnPrefix + "_issue_id"));
        columns.add(Column.aliased("issue_uuid", table, columnPrefix + "_issue_uuid"));
        columns.add(Column.aliased("username", table, columnPrefix + "_username"));
        columns.add(Column.aliased("role_key", table, columnPrefix + "_role_key"));
        columns.add(Column.aliased("role_weight", table, columnPrefix + "_role_weight"));
        columns.add(Column.aliased("is_manager", table, columnPrefix + "_is_manager"));
        columns.add(Column.aliased("department_key", table, columnPrefix + "_department_key"));
        columns.add(Column.aliased("department_weight", table, columnPrefix + "_department_weight"));
        columns.add(Column.aliased("assigned_by_username", table, columnPrefix + "_assigned_by_username"));
        columns.add(Column.aliased("created", table, columnPrefix + "_created"));
        columns.add(Column.aliased("modified", table, columnPrefix + "_modified"));
        columns.add(Column.aliased("accepted", table, columnPrefix + "_accepted"));
        columns.add(Column.aliased("microapp_left", table, columnPrefix + "_microapp_left"));
        columns.add(Column.aliased("closed", table, columnPrefix + "_closed"));
        columns.add(Column.aliased("archived", table, columnPrefix + "_archived"));

        return columns;
    }
}
