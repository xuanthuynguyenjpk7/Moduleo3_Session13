package Bai6;

import Bai6.ConnectionDB;
import Bai6.Task;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskManagement {

    // 1. Thêm công việc
    public void addTask(String taskName, String status) {

        String sql = "{CALL add_task(?, ?)}";

        try (CallableStatement stmt =
                     ConnectionDB.conn.prepareCall(sql)) {

            stmt.setString(1, taskName);
            stmt.setString(2, status);

            stmt.execute();

            System.out.println("Thêm công việc thành công!");

        } catch (SQLException e) {
            System.out.println("Lỗi khi thêm công việc!");
            e.printStackTrace();
        }
    }

    // 2. Liệt kê công việc
    public void listTasks() {

        String sql = "{CALL list_tasks()}";

        try (CallableStatement stmt =
                     ConnectionDB.conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("\n===== DANH SÁCH CÔNG VIỆC =====");

            while (rs.next()) {

                Task task = new Task();

                task.setId(rs.getInt("id"));
                task.setTaskName(rs.getString("task_name"));
                task.setStatus(rs.getString("status"));

                System.out.println(task);
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy danh sách!");
            e.printStackTrace();
        }
    }

    // 3. Cập nhật trạng thái
    public void updateTaskStatus(int taskId, String status) {

        String sql = "{CALL update_task_status(?, ?)}";

        try (CallableStatement stmt =
                     ConnectionDB.conn.prepareCall(sql)) {

            stmt.setInt(1, taskId);
            stmt.setString(2, status);

            int result = stmt.executeUpdate();

            if (result > 0) {
                System.out.println("Cập nhật trạng thái thành công!");
            } else {
                System.out.println("Không tìm thấy công việc có ID = " + taskId);
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi cập nhật!");
            e.printStackTrace();
        }
    }

    // 4. Xóa công việc
    public void deleteTask(int taskId) {

        String sql = "{CALL delete_task(?)}";

        try (CallableStatement stmt =
                     ConnectionDB.conn.prepareCall(sql)) {

            stmt.setInt(1, taskId);

            int result = stmt.executeUpdate();

            if (result > 0) {
                System.out.println("Xóa công việc thành công!");
            } else {
                System.out.println("Không tìm thấy công việc!");
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi xóa công việc!");
            e.printStackTrace();
        }
    }

    // 5. Tìm kiếm công việc
    public void searchTaskByName(String taskName) {

        String sql = "{CALL search_task_by_name(?)}";

        try (CallableStatement stmt =
                     ConnectionDB.conn.prepareCall(sql)) {

            stmt.setString(1, taskName);

            try (ResultSet rs = stmt.executeQuery()) {

                boolean found = false;

                System.out.println("\n===== KẾT QUẢ TÌM KIẾM =====");

                while (rs.next()) {

                    found = true;

                    Task task = new Task();

                    task.setId(rs.getInt("id"));
                    task.setTaskName(rs.getString("task_name"));
                    task.setStatus(rs.getString("status"));

                    System.out.println(task);
                }

                if (!found) {
                    System.out.println("Không tìm thấy công việc!");
                }
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi tìm kiếm!");
            e.printStackTrace();
        }
    }

    // 6. Thống kê công việc
    public void taskStatistics() {

        String sql = "{CALL task_statistics()}";

        try (CallableStatement stmt =
                     ConnectionDB.conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("\n===== THỐNG KÊ CÔNG VIỆC =====");

            while (rs.next()) {

                String status = rs.getString("status");
                int total = rs.getInt("total");

                System.out.println(
                        status + ": " + total + " công việc"
                );
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi thống kê!");
            e.printStackTrace();
        }
    }
}