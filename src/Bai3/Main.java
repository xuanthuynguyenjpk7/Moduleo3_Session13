package Bai3;

import Bai2.ConnectionDB;
import Bai2.Student;

import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        Scanner sc = new Scanner(System.in);
        int choice;

        do {

            System.out.println("========== STUDENT MANAGEMENT ==========");
            System.out.println("1. Hiển thị danh sách sinh viên");
            System.out.println("2. Thêm mới sinh viên");
            System.out.println("3. Sửa sinh viên");
            System.out.println("4. Xóa sinh viên");
            System.out.println("5. Tìm kiếm sinh viên");
            System.out.println("6. Thoát");
            System.out.println("========================================");

            System.out.println("Nhập lựa chọn: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    displayStudents();
                    break;
                case 2:
                    addStudent(sc);
                    break;
                case 3:
                    updateStudent(sc);
                    break;
                case 4:
                    deleteStudent(sc);
                    break;
                case 5:
                    searchStudent(sc);
                    break;
                case 6:
                    System.out.println("Đã thoát chương trình");
                    break;
                default:
                    System.out.println("Vui lòng nhập từ 1 đến 6!!");

            }
        } while (choice != 6);
        sc.close();
    }

    public static void displayStudents() throws SQLException {
        String sql = "{CALL get_all_students()}";
        Connection connection = ConnectionDB.getConnection();
        try (
                CallableStatement stmt = connection.prepareCall(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            System.out.println("--- ĐÃ KẾT NỐI VÀ CHẠY PROCEDURE ---");
            while (rs.next()) {
                int studentId = rs.getInt("student_id");
                String fullName = rs.getString("full_name");
                LocalDate dateOfBirth = rs.getDate("date_of_birth").toLocalDate();
                String email = rs.getString("email");
                System.out.println(studentId + " " + fullName + " " + dateOfBirth + " " + email);
            }
        }
    }


    private static void addStudent(Scanner sc) {
        // kiểm tra connect bị null ko
        if (ConnectionDB.conn == null) {
            System.out.println("Lỗi: Chưa kết nối được với Database!!");
            return;
        }

        // tạo đối tượng student
        Student student = new Student();
        // vì có thể đọc mất Enter và thành chuỗi rỗng nên có thêm dòng dưới
        sc.nextLine();

        //Nhập họ tên
        System.out.println("Nhập họ tên: ");
        String fullName = sc.nextLine();
        student.setFullName(fullName);

        //Nhập ngày sinh

        LocalDate dateOfBirth = null; //tạo biến dateofbirth cho ng dùng nhập vào, hiện tại chưa có giá trị

        // tạo vòng lặp while
        // Trong khi dateOfBirth vẫn chưa có giá trị thì tiếp tục cho người dùng nhập.
        while (dateOfBirth == null) {
            System.out.println("Nhập ngày sinh: ");
            String dateInput = sc.nextLine(); // Nhận dữ liệu từ người dùng nhập vào, gắn cho biến dateInput

            //nếu đúng → dateOfBirth có giá trị → thoát while
            //Nếu sai → catch → dateOfBirth vẫn là null → quay lại đầu while → cho nhập lại.
            // Ý nghĩa try catch: Thử thực hiện đoạn code này. Nếu xảy ra lỗi thì chuyển sang catch.
            try {
                dateOfBirth = LocalDate.parse(dateInput); //Thử chuyển dateInput từ String thành LocalDate, và gán vào biến dateofbirth
            } catch (Exception e) {
                System.out.println("Ngày sinh không hợp lệ, vui lòng nhập lại theo dạng yyyy-mm-dd.");
            }
        }
        student.setDateOfBirth(dateOfBirth); // đưa ngày sinh vừa nhập vào đối tượng student.


        //Nhập email
        String email = "";
        while (true) {
            System.out.println("Nhập email: ");
            email = sc.nextLine().trim();

            if (email.isEmpty()) {
                System.out.println("Email không được để trống!");
                continue;
            }
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                System.out.println("Email không hợp lệ, vui lòng nhập lại!");
                continue;
            }
            break;
        }
        student.setEmail(email);

        // Gọi stored procedure
        String sqlAdd = "{CALL add_student(?, ?, ?)}";

        // Dùng try-with-resources để tự động đóng Statement và ResultSet
        try (CallableStatement stmt = ConnectionDB.conn.prepareCall(sqlAdd);) {
            stmt.setString(1, student.getFullName());
            stmt.setDate(2, Date.valueOf(String.valueOf(Date.valueOf(student.getDateOfBirth()))));
            stmt.setString(3, student.getEmail());
            stmt.executeUpdate();
            System.out.println("Thêm sinh viên thành công!");

        } catch (SQLException e) {
            System.err.println("Lỗi SQL: " + e.getMessage());
        }
    }

    private static void updateStudent(Scanner sc) throws SQLException {
        // kiểm tra connect bị null ko
        if (ConnectionDB.conn == null) {
            System.out.println("Lỗi: Chưa kết nối được với Database!!");
            return;
        }

        //Nhập Student ID
        sc.nextLine();
        int studentId;
        while (true) {
            System.out.println("Nhập id sinh viên cần sửa: ");
            try {
                studentId = Integer.parseInt(sc.nextLine().trim());
                if (studentId <= 0) {
                    System.out.println("ID phải lớn hơn 0!");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("ID không hợp lệ, vui lòng nhập số nguyên!");
            }
        }

        //Tìm Student theo ID
        Student student = new Student();
        String sqlFind = "{CALL find_student_by_student_id(?)}";

        try (CallableStatement stmt = ConnectionDB.conn.prepareCall(sqlFind)) {
            stmt.setInt(1, studentId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("Không tìm thấy sinh viên có ID: " + studentId);
                    return;
                }

                student.setStudentId(rs.getInt("student_id"));
                student.setFullName(rs.getString("full_name"));
                student.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
                student.setEmail(rs.getString("email"));
            }
        } catch (SQLException e) {
            System.out.println("Lỗi SQL: " + e.getMessage());
            return;
        }
        //Nhập họ tên mới
        while (true) {
            System.out.println("Nhập họ tên mới của SV update: ");
            String fullName = sc.nextLine().trim();

            if (fullName.isEmpty()) {
                System.out.println("Họ tên không được để trống!");
                continue;
            }
            student.setFullName(fullName);
            break;
        }

        //Nhập ngày sinh mới
        LocalDate dateOfBirth = null;
        while (dateOfBirth == null) {
            System.out.println("Nhập ngày sinh mới của SV update: ");
            String dateInput = sc.nextLine().trim();
            try {
                dateOfBirth = LocalDate.parse(dateInput);
            } catch (Exception e) {
                System.out.println("Ngày sinh không hợp lệ!Vui lòng nhập lại!");
            }
        }
        student.setDateOfBirth(dateOfBirth);

        //Nhập email mới
        String email = "";
        while (true) {
            System.out.println("Nhập email mới của SV update: ");
            email = sc.nextLine().trim();

            if (email.isEmpty()) {
                System.out.println("Email không được để trống! Vui lòng nhập lại email: ");
                continue;
            }
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                System.out.println("Email không hợp lệ! Vui lòng nhập lại email: ");
                continue;
            }
            break;
        }
        student.setEmail(email);

        //Update database
        String sqlUpdate = "{CALL update_student(?, ?, ?, ?)}";

        try (CallableStatement stmt = ConnectionDB.conn.prepareCall(sqlUpdate)) {
            stmt.setInt(1, studentId);
            stmt.setString(2, student.getFullName());
            stmt.setDate(3, Date.valueOf(student.getDateOfBirth()));
            stmt.setString(4, student.getEmail());
            stmt.executeUpdate();
            System.out.println("Cập nhật sinh viên thành công!");
        } catch (SQLException e) {
            System.out.println("Lỗi SQL: " + e.getMessage());
        }

    }

    private static void deleteStudent(Scanner sc) {
        //Kiểm tra xem connect bị lỗi ko
        if (ConnectionDB.conn == null) {
            System.out.println("Lỗi: Chưa kết nối được với Database!!");
            return;
        }
        //Nhập id cần xóa của student
        sc.nextLine();
        int studentId;

        while (true) {
            System.out.println("Nhập mã ID sinh viên cần xóa: ");
            try {
                studentId = Integer.parseInt(sc.nextLine().trim());
                if (studentId <= 0) {
                    System.out.println("ID phải lớn hơn 0!");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("ID không hợp lệ! Vui lòng nhập số nguyên!");
            }

        }
        //Tìm xem ID có tồn tại hay không
        Student student = new Student();
        String sqlFind = "{CALL find_student_by_student_id(?)}";

        try (CallableStatement stmt = ConnectionDB.conn.prepareCall(sqlFind)) {
            stmt.setInt(1, studentId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("Không tìm thấy sinh viên có mã ID: " + studentId);
                    return;
                }
                student.setStudentId(rs.getInt("student_id"));
                student.setFullName(rs.getString("full_name"));
                student.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
                student.setEmail(rs.getString("email"));
            }

        } catch (SQLException e) {
            System.out.println("Lỗi SQL: " + e.getMessage());
        }

        //xóa sinh viên
        String sqlDelete = "{CALL delete_student(?)}";
        try (CallableStatement stmt = ConnectionDB.conn.prepareCall(sqlDelete)) {
            stmt.setInt(1, studentId);
            stmt.executeUpdate();
            System.out.println("Đã xóa sinh viên có mã ID " + studentId + " thành công!");
        } catch (SQLException e) {
            System.out.println("Lỗi SQL: " + e.getMessage());
        }
    }

    private static void searchStudent(Scanner sc) {
        if (ConnectionDB.conn == null) {
            System.out.println("Lỗi: Chưa kết nối được với database!");
            return;
        }
        //nhập tên sv cần tìm
        sc.nextLine();

        System.out.println("Nhập tên sinh viên cần tìm: ");
        String studentName = sc.nextLine().trim();

        String sqlSearch = "{CALL search_student(?)}";
        try (CallableStatement stmt = ConnectionDB.conn.prepareCall(sqlSearch)) {
            stmt.setString(1, studentName);
            try (ResultSet rs = stmt.executeQuery()) {
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    int studentId = rs.getInt("student_id");
                    String fullName = rs.getString("full_name");
                    Date dateOfBirth = Date.valueOf(rs.getDate("date_of_birth").toLocalDate());
                    String email = rs.getString("email");
                    System.out.println(studentId + " " + fullName + " " + dateOfBirth + " " + email);
                }
                if (!found) {
                    System.out.println("Không tìm thấy sinh viên!");
                }
            } catch (SQLException e) {
                System.out.println("Lỗi SQL: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Lỗi SQL: " + e.getMessage());
        }
    }
}

