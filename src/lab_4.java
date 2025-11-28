import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Scanner;
import java.time.LocalDate;
import java.lang.String;

public class lab_4 {
    public static Scanner sc = new Scanner(System.in);

    public static void printMenu() {
        System.out.println(
                "Choose an option from below (1-5): \n1. Display Schedule \n2. Edit Drivers \n3.Edit Busses \n4. Edit Trips \n5. Quit");
    }

    public static void displaySchedule(Connection con) throws Exception {
        System.out.println(
                "\nDisplay Schedule: \n1. See all trips for certain Starting Location (provide Starting Location, Destination, and Date) \n2. Show stops of certain trip (provide Trip Number) \n3. See weekly schedule (provide Driver and Date) \n4. Quit");
        int choice = sc.nextInt();
        sc.nextLine();
        Statement st = con.createStatement();

        switch (choice) {
            case 1: {
                System.out.print("Enter the starting location: ");
                String startLoc = sc.nextLine();
                System.out.print("Enter the destination: ");
                String dest = sc.nextLine();
                System.out.print("Enter the date: ");
                String date = sc.nextLine();
                ResultSet rs = st.executeQuery(String.format(
                        """
                                    SELECT
                                            t.StartLocationName,
                                            t.DestinationName,
                                            o.Date,
                                            o.ScheduledStartTime,
                                            o.SecheduledArrivalTime,
                                            o.DriverName,
                                            o.BusID
                                        FROM Trip AS t
                                        JOIN TripOffering AS o
                                            ON t.TripNumber = o.TripNumber
                                        WHERE t.StartLocationName = '%s'
                                            AND t.DestinationName   = '%s'
                                            AND o.Date              = '%s';
                                """, startLoc, dest, date));
                if (!rs.next()) {
                    System.out.println("No trips were found.\n");
                    return;
                }
                while (rs.next()) {
                    System.out.printf(
                            "%s -> %s on %s | %s - %s | Driver: %s | Bus: %s%n",
                            rs.getString("StartLocationName"),
                            rs.getString("DestinationName"),
                            rs.getString("Date"),
                            rs.getString("ScheduledStartTime"),
                            rs.getString("SecheduledArrivalTime"),
                            rs.getString("DriverName"),
                            rs.getString("BusID"));
                }

                break;
            }

            case 2: {
                System.out.print("Enter the TripNumber: ");
                int tripNum = sc.nextInt();
                ResultSet rs = st.executeQuery(String.format(
                        "SELECT t.StopNumber, s.StopAddress FROM TripStopInfo as t JOIN TripStop AS s ON t.StopNumber = s.StopNumber WHERE t.TripNumber = %d ORDER BY StopNumber ASC",
                        tripNum));
                if (!rs.next()) {
                    System.out.println("No trips were found.\n");
                    return;
                }
                while (rs.next()) {
                    System.out.printf("Stop #%d: %s%n", rs.getInt("StopNumber"), rs.getString("StopAddress"));
                }
                sc.nextLine();

                break;
            }

            case 3: {
                System.out.print("Enter the driver name: ");
                String driver = sc.nextLine();

                System.out.print("Enter the starting date (YYYY-MM-DD): ");
                String startDateStr = sc.nextLine();

                LocalDate startDate = LocalDate.parse(startDateStr);
                LocalDate endDate = startDate.plusDays(6);
                String endDateStr = endDate.toString();

                ResultSet rs = st.executeQuery(String.format(
                        """
                                SELECT
                                    TripNumber,
                                    Date,
                                    ScheduledStartTime,
                                    SecheduledArrivalTime,
                                    BusID
                                FROM TripOffering
                                WHERE DriverName = '%s'
                                AND Date BETWEEN '%s' AND '%s'
                                ORDER BY Date, ScheduledStartTime
                                """,
                        driver, startDateStr, endDateStr));

                if (!rs.next()) {
                    System.out.println("No trips were found.\n");
                    return;
                }

                while (rs.next()) {
                    System.out.printf(
                            "Date: %s | Trip: %d | %s - %s | Bus: %s%n",
                            rs.getString("Date"),
                            rs.getInt("TripNumber"),
                            rs.getString("ScheduledStartTime"),
                            rs.getString("SecheduledArrivalTime"),
                            rs.getString("BusID"));
                }

                break;
            }
            case 4:
                return;

            default: {
                System.out.println("\nInvalid Choice. Select an option between 1-4.");
                displaySchedule(con);
            }
        }
    }

    public static void editDrivers(Connection con) throws Exception {
        System.out.println(
                "\nEdit Drivers: \n1. Add a driver \n2. Change driver for trip (provide Trip Number) \n3. Delete driver \n4. Quit");
        int choice = sc.nextInt();

        switch (choice) {
            case 1: {

                break;
            }

            case 2: {
                break;
            }

            case 3: {
                break;
            }
            default: {
                System.out.println("\nInvalid Choice. Select an option between 1-3.");
                editDrivers(con);
            }
        }
    }

    public static void editBusses(Connection con) throws Exception {
        System.out.println(
                "\nEdit Busses: \n1. Add a bus \n2. Change bus for trip (provide Trip Number) \n3. Delete bus \n4. Quit");
        int choice = sc.nextInt();
        Statement st = con.createStatement();

        switch (choice) {
            case 1: {
                System.out.print("Enter the Bus ID: ");
                int busID = sc.nextInt();
                System.out.print("Enter the Bus Year: ");
                int busYear = sc.nextInt();
                sc.nextLine();
                System.out.print("Enter the Bus Model: ");
                String busModel = sc.nextLine();
                ResultSet rs = st.executeQuery(
                        String.format("SELECT * FROM Bus WHERE BusID = %d  AND busModel = '%s' AND Year = %d", busID,
                                busModel, busYear));
                if (rs.next()) {
                    System.out.println("Bus already exists.\n");
                    return;
                }

            }
            case 2: {

                break;
            }
            case 3: {
                break;
            }
            default: {
                System.out.println("\nInvalid Choice. Select an option between 1-3.");
                editBusses(con);
            }
        }
    }

    public static void editTrips(Connection con) throws Exception {
        System.out.println("\nEdit Trips: \n1. Add Trip Offering(s) \n2. Delete Trip Offering");
        int choice = sc.nextInt();

        switch (choice) {
            case 1: {
                break;
            }
            case 2: {
                break;
            }
            default: {
                System.out.println("\nInvalid Choice. Select an option between 1-2.");
                editTrips(con);
            }
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
            sc.nextLine();

            switch (option) {
                case 1:
                    displaySchedule(con);
                    break;
                case 2:
                    editDrivers(con);
                    break;
                case 3:
                    editBusses(con);
                    break;
                case 4:
                    editTrips(con);
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Invalid Option, choose between 1-5.\n");
            }
        } while (option != 6);

        con.close();
        sc.close();
    }

}
