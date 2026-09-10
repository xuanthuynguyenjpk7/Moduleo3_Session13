package Bai6;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {

    public static Connection conn;

    public static void openConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/todo_management";
            String username = "root";
            String password = "123456";

            conn = DriverManager.getConnection(
                    url,
                    username,
                    password
            );

            System.out.println("Kết nối database thành công!");

        } catch (SQLException e) {
            System.out.println("Kết nối database thất bại!");
            e.printStackTrace();
        }
    }

    public static void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Đã đóng kết nối!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
