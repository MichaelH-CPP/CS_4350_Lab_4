import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

public class lab_4 {
    public static Scanner sc = new Scanner(System.in);

    public static void printMenu() {
        System.out.println(
                "Choose an option from below (1-5): \n1. Display Schedule \n2. Edit Drivers \n3.Edit Busses\n 4. Edit Trips \n 5. Quit");
    }

    public static void displaySchedule() {
        System.out.println(
                "\nDisplay Schedule: \n1. See all trips for certain Starting Location (provide Starting Location, Destination, and Date) \n2. Show stops of certain trip (provide Trip Number) \n3. See weekly schedule (provide Driver and Date) \n4. Quit");

        int choice = sc.nextInt();

        switch (choice) {
            case 1:
                break;

            case 2:
                break;

            case 3:
                break;

            case 4:
                break;

            default:
                System.out.println("\nInvalid Choice. Select an option between 1-4.");
                displaySchedule();
        }
    }

    public static void editDrivers() {
        System.out.println(
                "\nEdit Drivers: \n1. Add a driver \n2. Change driver for trip (provide Trip Number) \n3. Delete driver \n4. Quit");
        int choice = sc.nextInt();

        switch (choice) {
            case 1:
                break;

            case 2:
                break;

            case 3:
                break;

            default:
                System.out.println("\nInvalid Choice. Select an option between 1-3.");
                editDrivers();
        }
    }

    public static void editBusses() {
        System.out.println(
                "\nEdit Busses: \n1. Add a bus \n2. Change bus for trip (provide Trip Number) \n3. Delete bus \n4. Quit");
        int choice = sc.nextInt();

        switch (choice) {
            case 1:
                break;

            case 2:
                break;

            case 3:
                break;

            default:
                System.out.println("\nInvalid Choice. Select an option between 1-3.");
                editBusses();
        }
    }

    public static void editTrips() {
        System.out.println("\nEdit Trips: \n1. Add Trip Offering(s) \n2. Delete Trip Offering");
        int choice = sc.nextInt();

        switch (choice) {
            case 1:
                break;

            case 2:
                break;

            default:
                System.out.println("\nInvalid Choice. Select an option between 1-2.");
                editTrips();
        }
    }

    public static void main(String[] args) throws Exception {
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/lab4?useSSL=false&serverTimezone=UTC",
                "cs4350", "pass");

        int option = 0;
        do {
            printMenu();
            option = sc.nextInt();

            switch (option) {
                case 1:
                    displaySchedule();
                    break;
                case 2:
                    editDrivers();
                    break;
                case 3:
                    editBusses();
                    break;
                case 4:
                    editTrips();
                case 5:
                    break;
                default:
                    System.out.println("Invalid Option, choose between 1-5.\n");
                    continue;
            }
        } while (option != 6);

        con.close();
        sc.close();
    }

}
