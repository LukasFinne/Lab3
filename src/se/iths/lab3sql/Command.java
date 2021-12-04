package se.iths.lab3sql;

import java.sql.SQLException;

@FunctionalInterface
public interface Command {
    void execute() throws SQLException;
}
