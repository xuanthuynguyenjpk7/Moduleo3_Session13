package Bai2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    public static Connection conn = null;

    static {
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/student_management",
                    "root",
                    "123456");
        } catch (SQLException e) {
            System.out.println("Kết nối thất bại!");
        }
    }

    public static Connection getConnection() {
        return conn;
    }
}
