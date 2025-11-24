import java.sql.Connection;
import java.sql.DriverManager;

public class lab_4 {
    public static void main(String[] args) throws Exception {
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/lab4?useSSL=false&serverTimezone=UTC", "cs4350", "pass");
        System.out.println("Connected!");
        con.close();
    }
}
