package se.iths.lab3sql;

import java.sql.*;
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final Command[] commands = new Command[12];
    SqlMethods s = new SqlMethods();

    public Main() {
        commands[1] = () -> s.showAll();
        commands[2] = () -> s.addNewArtist(sc);
        commands[3] = () -> s.deleteArtist(sc);
        commands[4] = () -> s.updateFirstName(sc);
        commands[5] = () -> s.updateLastName(sc);
        commands[6] = () -> s.updateAge(sc);
        commands[7] = () -> s.findById(sc);
        commands[8] = () -> s.findByAge(sc);
        commands[9] = () -> s.findByName(sc);
        commands[10] = () -> s.createTableAndTrigger();
        commands[0] = this::shutdown;
    }

    public static void main(String[] args) throws SQLException {
        Main main = new Main();
        main.run();
    }


    public void run() throws SQLException {
        int choice = 0;
        s.connect();
        do {
            printMenuOption();
            try {
                choice = readChoice(sc);
            } catch (Exception e) {
                System.out.println("Only numbers please");
            }
            executeChoice(choice);
        } while (choice != 0);
    }

    private void shutdown() {
        System.exit(0);
    }

    private void executeChoice(int choice) throws SQLException {
        commands[choice].execute();
    }

    private int readChoice(Scanner sc) {
        return sc.nextInt();
    }

    private void printMenuOption() {
        System.out.println("1. Show all");
        System.out.println("2. Add new artist");
        System.out.println("3. Delete artist");
        System.out.println("4. Update an artist First name");
        System.out.println("5. Update an artist Last name");
        System.out.println("6. Update an artist Age");
        System.out.println("7. Find by Id");
        System.out.println("8. Find by Age");
        System.out.println("9. Find by Name");
        System.out.println("10. Create the table and triggers for insertion and update");
        System.out.println("0. Exit program");
    }

}
