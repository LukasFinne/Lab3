package se.iths.lab3sql;

import java.sql.*;
import java.util.Scanner;

public class SqlMethods {
    Connection connection;


    public void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/laboration3", "javaConnect", "password123");
    }

    public void createTableAndTrigger() throws SQLException {
        try {
            createTable();
            createInsertTrigger();
            createUpdateTrigger();
        } catch (SQLSyntaxErrorException e) {
            System.out.println("The table already exist");
        }

    }

    public void createTable() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE artist(first_name VARCHAR(45) NOT NULL ,last_name VARCHAR(45)  NOT NULL ,age smallint UNSIGNED, id smallint AUTO_INCREMENT, PRIMARY KEY (id))");
        preparedStatement.executeUpdate();
    }

    public void createInsertTrigger() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "CREATE TRIGGER no_numbers_trigger " +
                        "BEFORE INSERT ON artist  FOR EACH ROW " +
                        "BEGIN " +
                        "IF NEW.first_name REGEXP '^[0-9]+$' " +
                        "THEN " +
                        "SIGNAL SQLSTATE '45000' " +
                        "SET MESSAGE_TEXT = \"Please do not use numbers in the artist name\"; " +
                        "END IF; " +
                        "IF NEW.last_name REGEXP '^[0-9]+$' " +
                        "THEN " +
                        "SIGNAL SQLSTATE '45000' " +
                        "SET MESSAGE_TEXT = \"Please do not use numbers in the artist name\"; " +
                        "END IF; " +
                        "END; "
        );
        preparedStatement.executeUpdate();
    }

    public void createUpdateTrigger() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "CREATE TRIGGER no_numbers_trigger_update " +
                        "BEFORE UPDATE ON artist  FOR EACH ROW " +
                        "BEGIN " +
                        "IF NEW.first_name REGEXP '^[0-9]+$' " +
                        "THEN " +
                        "SIGNAL SQLSTATE '45000' " +
                        "SET MESSAGE_TEXT = \"Please do not use numbers in the artist name\"; " +
                        "END IF; " +
                        "IF NEW.last_name REGEXP '^[0-9]+$' " +
                        "THEN " +
                        "SIGNAL SQLSTATE '45000' " +
                        "SET MESSAGE_TEXT = \"Please do not use numbers in the artist name\"; " +
                        "END IF; " +
                        "END; "
        );
        preparedStatement.executeUpdate();
    }


    public void addNewArtist(Scanner sc) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO artist(first_name,last_name,age) VALUES (?,?,?)");
        System.out.println("Write the First name of the artist");
        preparedStatement.setString(1, sc.next());
        System.out.println("Write the Last name of the artist");
        preparedStatement.setString(2, sc.next());
        System.out.println("Write the Age of the artist");
        preparedStatement.setString(3, sc.next());
        try {
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Wrong Input!, Please do not use numerics for artist name");
            addNewArtist(sc);
        }

    }

    public void deleteArtist(Scanner sc) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM artist WHERE id = ?");
        System.out.println("Write the id of the artist you want to delete");
        preparedStatement.setString(1, sc.next());
        try {
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Wrong Input!, Please do not use numerics for artist name");
            deleteArtist(sc);
        }
    }

    public void updateFirstName(Scanner sc) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE artist SET first_name=? WHERE id = ?");
        System.out.println("Write the updated First name!");
        updateArtistId(sc, preparedStatement);
        try {
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Wrong Input!, Please do not use numerics for artist name");
            updateFirstName(sc);
        }
    }

    public void updateLastName(Scanner sc) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE artist SET last_name=? WHERE id = ?");
        System.out.println("Write the updated Last name!");
        updateArtistId(sc, preparedStatement);
        try {
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Wrong Input!, Please do not use numerics for artist name");
            updateLastName(sc);
        }
    }

    public void updateAge(Scanner sc) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE artist SET age =? WHERE id = ?");
        System.out.println("Write the updated Age");
        updateArtistId(sc, preparedStatement);
        try {
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Wrong Input!, Please do not use numerics for artist name");
            updateAge(sc);
        }

    }

    private void updateArtistId(Scanner sc, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, sc.next());
        System.out.println("Write the id of the artist you want to update");
        preparedStatement.setString(2, sc.next());
    }

    public void showAll() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM artist");
        printColumns(preparedStatement);
    }

    public void findById(Scanner sc) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM artist WHERE id=?");
        System.out.println("Write the id of the artist you want to find");
        preparedStatement.setString(1, sc.next());
        try {
            printColumns(preparedStatement);
        } catch (Exception e) {
            System.out.println("Wrong Input!, Please do not use numerics for artist name");
            findById(sc);
        }


    }

    public void findByAge(Scanner sc) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM artist WHERE age=?");
        System.out.println("Write the age of the artist you want to find");
        preparedStatement.setString(1, sc.next());
        try {
            printColumns(preparedStatement);
        } catch (Exception e) {
            System.out.println("Wrong Input!, Please do not use numerics for artist name");
            findByAge(sc);
        }

    }

    public void findByName(Scanner sc) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM artist WHERE CONCAT(first_name,' ', last_name ) LIKE ?");
        System.out.println("Write the name of the artist you want to find");
        preparedStatement.setString(1, "%" + sc.next() + "%");
        try {
            printColumns(preparedStatement);
        } catch (Exception e) {
            System.out.println("Wrong Input!, Please do not use numerics for artist name");
            findByName(sc);
        }

    }

    private void printColumns(PreparedStatement preparedStatement) throws SQLException {
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            System.out.println("Artist: " + resultSet.getString("first_name") + " " + resultSet.getString("last_name")
                    + ", Age:" + resultSet.getInt("age") + ", ID:" + resultSet.getInt("id")
            );
        }
    }
}
