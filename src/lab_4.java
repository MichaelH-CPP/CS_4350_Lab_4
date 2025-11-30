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
                "\nChoose an option from below (1-5): \n1. Display Schedule \n2. Edit Drivers \n3.Edit Busses \n4. Edit Trips \n5. Quit");
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
                                            o.DateOfTrip,
                                            o.ScheduledStartTime,
                                            o.ScheduledArrivalTime,
                                            o.DriverName,
                                            o.BusID
                                        FROM Trip AS t
                                        JOIN TripOffering AS o
                                            ON t.TripNumber = o.TripNumber
                                        WHERE t.StartLocationName = '%s'
                                            AND t.DestinationName   = '%s'
                                            AND o.DateofTrip              = '%s';
                                """, startLoc, dest, date));
                if (!rs.next()) {
                    System.out.println("No trips were found.\n");
                    rs.close();
                    st.close();
                    return;
                }
                while (rs.next()) {
                    System.out.printf(
                            "%s -> %s on %s | %s - %s | Driver: %s | Bus: %s%n",
                            rs.getString("StartLocationName"),
                            rs.getString("DestinationName"),
                            rs.getString("DateOfTrip"),
                            rs.getString("ScheduledStartTime"),
                            rs.getString("ScheduledArrivalTime"),
                            rs.getString("DriverName"),
                            rs.getString("BusID"));
                }
                rs.close();
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
                    rs.close();
                    st.close();
                    return;
                }
                while (rs.next()) {
                    System.out.printf("Stop #%d: %s%n", rs.getInt("StopNumber"), rs.getString("StopAddress"));
                }
                sc.nextLine();
                rs.close();

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
                                    DateOfTrip,
                                    ScheduledStartTime,
                                    ScheduledArrivalTime,
                                    BusID
                                FROM TripOffering
                                WHERE DriverName = '%s'
                                AND Date BETWEEN '%s' AND '%s'
                                ORDER BY Date, ScheduledStartTime
                                """,
                        driver, startDateStr, endDateStr));

                if (!rs.next()) {
                    System.out.println("No trips were found.\n");
                    rs.close();
                    st.close();
                    return;
                }

                while (rs.next()) {
                    System.out.printf(
                            "Date: %s | Trip: %d | %s - %s | Bus: %s%n",
                            rs.getString("DateOfTrip"),
                            rs.getInt("TripNumber"),
                            rs.getString("ScheduledStartTime"),
                            rs.getString("ScheduledArrivalTime"),
                            rs.getString("BusID"));
                }

                break;
            }
            case 4:
                st.close();
                return;
            default: {
                System.out.println("\nInvalid Choice. Select an option between 1-4.");
                displaySchedule(con);
            }
        }
        st.close();

    }

    public static void editDrivers(Connection con) throws Exception {
        System.out.println(
                "\nEdit Drivers: \n1. Add a driver \n2. Change driver for trip (provide Trip Number) \n3. Delete driver \n4. Quit");
        int choice = sc.nextInt();
        sc.nextLine();
        Statement st = con.createStatement();

        switch (choice) {
            case 1: {
                System.out.print("Enter the driver name: ");
                String driverName = sc.nextLine();
                System.out.print("Enter the driver telephone number: ");
                String phone = sc.nextLine();

                ResultSet rs = st.executeQuery(String.format(
                        "SELECT * FROM Driver WHERE DriverName = '%s'", driverName));
                if (rs.next()) {
                    System.out.println("Driver already exists.\n");
                    rs.close();
                    st.close();
                    return;
                }

                int result = st.executeUpdate(String.format(
                        "INSERT INTO Driver(DriverName, DriverTelephoneNumber) VALUES('%s', '%s');",
                        driverName, phone));
                if (result == 0) {
                    System.out.println("Issue with insert.");
                    rs.close();
                    st.close();
                    return;
                } else {
                    System.out.println("Driver added successfully!\n");
                }
                rs.close();
                break;
            }

            case 2: {
                System.out.print("Enter the trip number: ");
                int tripNumber = sc.nextInt();
                sc.nextLine();

                ResultSet rs = st.executeQuery(String.format(
                        "SELECT * FROM TripOffering WHERE TripNumber = %d", tripNumber));
                if (!rs.next()) {
                    System.out.println("No trip offering exists for that trip number.\n");
                    rs.close();
                    st.close();
                    return;
                }
                rs.close();

                rs = st.executeQuery("SELECT * FROM Driver");
                if (!rs.next()) {
                    System.out.println("No drivers found.\n");
                    rs.close();
                    st.close();
                    return;
                }

                System.out.println("Here is the list of available drivers: ");
                do {
                    System.out.println(String.format("Name: %s, Phone: %s",
                            rs.getString("DriverName"),
                            rs.getString("DriverTelephoneNumber")));
                } while (rs.next());
                rs.close();

                System.out.print("Enter the driver name you'd like to assign: ");
                String newDriver = sc.nextLine();

                int result = st.executeUpdate(String.format(
                        "UPDATE TripOffering SET DriverName='%s' WHERE TripNumber=%d",
                        newDriver, tripNumber));
                if (result == 0) {
                    System.out.println("Update was unsuccesful.");
                } else {
                    System.out.println("Update was successful!");
                }
                break;
            }

            case 3: {
                System.out.print("Enter the name of the driver you'd like to delete: ");
                String driverName = sc.nextLine();

                ResultSet rs = st.executeQuery(String.format(
                        "SELECT * FROM Driver WHERE DriverName = '%s'", driverName));
                if (!rs.next()) {
                    System.out.println("Driver not found.");
                    rs.close();
                    st.close();
                    return;
                }

                int result = st.executeUpdate(String.format(
                        "DELETE FROM Driver WHERE DriverName='%s'", driverName));
                if (result == 0) {
                    System.out.println("Delete was unsuccesful.");
                } else {
                    System.out.println("Delete was successful!");
                }
                rs.close();
                break;
            }

            case 4: {
                st.close();
                return;
            }

            default: {
                System.out.println("\nInvalid Choice. Select an option between 1-3.");
                st.close();
                editDrivers(con);
            }
        }
        st.close();
    }

    public static void editBusses(Connection con) throws Exception {
        System.out.println(
                "\nEdit Busses: \n1. Add a bus \n2. Change bus for trip (provide Trip Number) \n3. Delete bus \n4. Quit");
        int choice = sc.nextInt();
        sc.nextLine();
        Statement st = con.createStatement();

        switch (choice) {
            case 1: {
                System.out.print("Enter the Bus ID: ");
                int busID = sc.nextInt();
                sc.nextLine();
                System.out.print("Enter the Bus Model: ");
                String model = sc.nextLine();
                System.out.print("Enter the Bus Year: ");
                int year = sc.nextInt();
                ResultSet rs = st.executeQuery(
                        String.format("SELECT * FROM Bus WHERE BusID = %d",
                                busID));
                if (rs.next()) {
                    System.out.println("Bus already exists.\n");
                    rs.close();
                    st.close();
                    return;
                }
                int result = st.executeUpdate(String
                        .format("INSERT INTO Bus(BusID, Model, YearOfBus) VALUES(%d,'%s', %d);", busID, model, year));
                if (result == 0) {
                    System.out.println("Issue with insert.");
                    rs.close();
                    st.close();
                    return;
                } else {
                    System.out.println("Insert successful!");
                }
                rs.close();
                break;
            }
            case 2: {
                System.out.print("Enter the trip number: ");
                int tripNumber = sc.nextInt();
                ResultSet rs = st.executeQuery(String.format("SELECT * FROM Trip WHERE TripNumber = %d", tripNumber));
                if (!rs.next()) {
                    System.out.println("No trip exists for that trip number.");
                    rs.close();
                    st.close();
                    return;
                }

                rs = st.executeQuery(String.format("""
                            SELECT *
                        FROM Bus AS b
                        WHERE b.BusID NOT IN (
                            SELECT t.BusID
                            FROM TripOffering AS t
                            WHERE t.DateOfTrip IN (
                                SELECT DateOfTrip
                                FROM TripOffering
                                WHERE TripNumber = %d
                            )
                        );

                        """, tripNumber));
                if (!rs.next()) {
                    System.out.println("No available busses");
                    rs.close();
                    st.close();
                    return;
                }
                System.out.println("Here is the list of available busses: ");
                do {
                    System.out.println(
                            String.format("BusID: %d, Model: %s, Year: %s", rs.getInt("BusID"), rs.getString("Model"),
                                    rs.getInt("YearOfBus")));
                } while (rs.next());
                System.out.println("Enter the BusID you'd like to choose: ");
                int busID = sc.nextInt();
                int result = st.executeUpdate(
                        String.format("UPDATE TripOffering SET BusID=%d WHERE TripNumber=%d", busID, tripNumber));
                if (result == 0) {
                    System.out.println("Update was unsuccesful.");
                } else {
                    System.out.println("Update was successful!");
                }
                rs.close();
                break;
            }
            case 3: {
                System.out.print("Enter the BusID of the bus you'd like to delete: ");
                int busID = sc.nextInt();
                ResultSet rs = st.executeQuery(String.format("SELECT * FROM Bus WHERE BusID = %d", busID));
                if (!rs.next()) {
                    System.out.println("Bus not found.");
                    rs.close();
                    st.close();
                    return;
                }
                int result = st.executeUpdate(String.format("DELETE FROM Bus WHERE BusID=%d", busID));
                if (result == 0) {
                    System.out.println("Delete was unsuccesful.");
                } else {
                    System.out.println("Delete was successful!");
                }
                rs.close();
                break;
            }
            default: {
                System.out.println("\nInvalid Choice. Select an option between 1-3.");
                editBusses(con);
            }
        }
        st.close();

    }

 public static void editTrips(Connection con) throws Exception {
    System.out.println("\nEdit Trips: \n1. Add Trip Offering(s) \n2. Delete Trip Offering \n3. Record Actual Trip Stop Info \n4. Quit");
    int choice = sc.nextInt();
    sc.nextLine();
    Statement st = con.createStatement();

    switch (choice) {
        // 1. Add Trip Offering(s)
        case 1: {
            String again = "y";
            while (again.equalsIgnoreCase("y")) {
                System.out.print("Enter the TripNumber: ");
                int tripNumber = sc.nextInt();
                sc.nextLine();

                System.out.print("Enter the date of trip (YYYY-MM-DD): ");
                String dateStr = sc.nextLine();

                System.out.print("Enter the scheduled start time (HH:MM:SS): ");
                String startTimeStr = sc.nextLine();

                System.out.print("Enter the scheduled arrival time (HH:MM:SS): ");
                String arrivalTimeStr = sc.nextLine();

                System.out.print("Enter the driver name: ");
                String driverName = sc.nextLine();

                System.out.print("Enter the BusID: ");
                int busID = sc.nextInt();
                sc.nextLine();

                ResultSet rs = st.executeQuery(String.format(
                        "SELECT * FROM TripOffering " +
                                "WHERE TripNumber = %d " +
                                "AND DateOfTrip = '%s' " +
                                "AND ScheduledStartTime = '%s'",
                        tripNumber, dateStr, startTimeStr));
                if (rs.next()) {
                    System.out.println("Trip offering already exists for that Trip/Date/StartTime.\n");
                    rs.close();
                } else {
                    rs.close();
                    int result = st.executeUpdate(String.format(
                            "INSERT INTO TripOffering(TripNumber, DateOfTrip, ScheduledStartTime, ScheduledArrivalTime, DriverName, BusID) "
                                    +
                                    "VALUES(%d, '%s', '%s', '%s', '%s', %d);",
                            tripNumber, dateStr, startTimeStr, arrivalTimeStr, driverName, busID));
                    if (result == 0) {
                        System.out.println("Issue with insert.\n");
                    } else {
                        System.out.println("Trip offering added successfully!\n");
                    }
                }

                System.out.print("Add another trip offering? (y/n): ");
                again = sc.nextLine();
            }
            break;
        }

        // 2. Delete Trip Offering
        case 2: {
            System.out.print("Enter the TripNumber: ");
            int tripNumber = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter the date of trip (YYYY-MM-DD): ");
            String dateStr = sc.nextLine();

            System.out.print("Enter the scheduled start time (HH:MM:SS): ");
            String startTimeStr = sc.nextLine();

            ResultSet rs = st.executeQuery(String.format(
                    "SELECT * FROM TripOffering " +
                            "WHERE TripNumber = %d " +
                            "AND DateOfTrip = '%s' " +
                            "AND ScheduledStartTime = '%s'",
                    tripNumber, dateStr, startTimeStr));
            if (!rs.next()) {
                System.out.println("Trip offering not found.\n");
                rs.close();
                st.close();
                return;
            }
            rs.close();

            int result = st.executeUpdate(String.format(
                    "DELETE FROM TripOffering " +
                            "WHERE TripNumber = %d " +
                            "AND DateOfTrip = '%s' " +
                            "AND ScheduledStartTime = '%s'",
                    tripNumber, dateStr, startTimeStr));
            if (result == 0) {
                System.out.println("Delete was unsuccesful.");
            } else {
                System.out.println("Delete was successful!");
            }

            break;
        }

        // 3. Record Actual Trip Stop Info
        case 3: {
            st.close();          // close this Statement
            recordActualTripInfo(con);  // call the new method
            return;              // return to main menu
        }

        // 4. Quit this submenu
        case 4: {
            st.close();
            return;
        }

        default: {
            System.out.println("\nInvalid Choice. Select an option between 1-4.");
        }
    }
    st.close();
}


    public static void recordActualTripInfo(Connection con) throws Exception {
    Statement st = con.createStatement();

    System.out.print("Enter the TripNumber: ");
    int tripNumber = sc.nextInt();
    sc.nextLine();

    System.out.print("Enter the date of trip (YYYY-MM-DD): ");
    String dateStr = sc.nextLine();

    System.out.print("Enter the scheduled start time (HH:MM:SS): ");
    String startTimeStr = sc.nextLine();

  
    ResultSet rs = st.executeQuery(String.format(
            "SELECT * FROM TripOffering " +
            "WHERE TripNumber = %d " +
            "AND DateOfTrip = '%s' " +
            "AND ScheduledStartTime = '%s'",
            tripNumber, dateStr, startTimeStr));
    if (!rs.next()) {
        System.out.println("No trip offering found for that trip/date/start time.\n");
        rs.close();
        st.close();
        return;
    }
    rs.close();


    System.out.print("Enter the StopNumber: ");
    int stopNumber = sc.nextInt();
    sc.nextLine();

  
    rs = st.executeQuery(String.format(
            "SELECT * FROM TripStopInfo " +
            "WHERE TripNumber = %d AND StopNumber = %d",
            tripNumber, stopNumber));
    if (!rs.next()) {
        System.out.println("No stop with that StopNumber is defined for this trip.\n");
        rs.close();
        st.close();
        return;
    }
    rs.close();

  
    System.out.print("Enter scheduled arrival time at this stop (HH:MM:SS): ");
    String schedArrStr = sc.nextLine();

    System.out.print("Enter actual start time at this stop (HH:MM:SS): ");
    String actualStartStr = sc.nextLine();

    System.out.print("Enter actual arrival time at this stop (HH:MM:SS): ");
    String actualArrStr = sc.nextLine();

    System.out.print("Enter number of passengers getting ON at this stop: ");
    int inCount = sc.nextInt();
    sc.nextLine();

    System.out.print("Enter number of passengers getting OFF at this stop: ");
    int outCount = sc.nextInt();
    sc.nextLine();

  
    rs = st.executeQuery(String.format(
            "SELECT * FROM ActualTripStopInfo " +
            "WHERE TripNumber = %d " +
            "AND DateOfTrip = '%s' " +
            "AND ScheduledStartTime = '%s' " +
            "AND StopNumber = %d",
            tripNumber, dateStr, startTimeStr, stopNumber));
    if (rs.next()) {
        System.out.println("Actual trip stop info already exists for this trip/stop.\n");
        rs.close();
        st.close();
        return;
    }
    rs.close();

   
    int result = st.executeUpdate(String.format(
            "INSERT INTO ActualTripStopInfo " +
            "(TripNumber, DateOfTrip, ScheduledStartTime, StopNumber, " +
            " ScheduledArrivalTime, ActualStartTime, ActualArrivalTime, " +
            " NumberOfPassengerIn, NumberOfPassengerOut) " +
            "VALUES (%d, '%s', '%s', %d, '%s', '%s', '%s', %d, %d)",
            tripNumber, dateStr, startTimeStr, stopNumber,
            schedArrStr, actualStartStr, actualArrStr,
            inCount, outCount));

    if (result == 0) {
        System.out.println("Insert was unsuccessful.\n");
    } else {
        System.out.println("Actual trip stop info inserted successfully!\n");
    }

    st.close();
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
        } while (option != 5);

        con.close();
        sc.close();
    }

}
