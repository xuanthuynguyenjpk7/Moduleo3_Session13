package Bai6;

import Bai6.ConnectionDB;
import Bai6.TaskManagement;

import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static TaskManagement taskManagement = new TaskManagement();

    public static void main(String[] args) {

        ConnectionDB.openConnection();

        int choice;

        do {
            showMenu();

            try {
                System.out.print("Nhập lựa chọn: ");
                choice = Integer.parseInt(sc.nextLine());

                switch (choice) {

                    case 1:
                        addTask();
                        break;

                    case 2:
                        taskManagement.listTasks();
                        break;

                    case 3:
                        updateTaskStatus();
                        break;

                    case 4:
                        deleteTask();
                        break;

                    case 5:
                        searchTask();
                        break;

                    case 6:
                        taskManagement.taskStatistics();
                        break;

                    case 0:
                        System.out.println("Thoát chương trình!");
                        break;

                    default:
                        System.out.println("Lựa chọn không hợp lệ!");
                }

            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số!");
                choice = -1;
            }

        } while (choice != 0);

        ConnectionDB.closeConnection();
    }

    // ==========================
    // MENU
    // ==========================

    public static void showMenu() {

        System.out.println("\n===== TO-DO LIST =====");
        System.out.println("1. Thêm công việc");
        System.out.println("2. Liệt kê công việc");
        System.out.println("3. Cập nhật trạng thái");
        System.out.println("4. Xóa công việc");
        System.out.println("5. Tìm kiếm công việc");
        System.out.println("6. Thống kê công việc");
        System.out.println("0. Thoát");
    }

    // ==========================
    // ADD
    // ==========================

    public static void addTask() {

        try {

            System.out.print("Nhập tên công việc: ");
            String taskName = sc.nextLine().trim();

            if (taskName.isEmpty()) {
                throw new IllegalArgumentException(
                        "Tên công việc không được để trống!"
                );
            }

            String status = inputStatus();

            taskManagement.addTask(taskName, status);

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    // ==========================
    // UPDATE
    // ==========================

    public static void updateTaskStatus() {

        try {

            System.out.print("Nhập ID công việc: ");
            int taskId = Integer.parseInt(sc.nextLine());

            if (taskId <= 0) {
                throw new IllegalArgumentException(
                        "ID phải lớn hơn 0!"
                );
            }

            String status = inputStatus();

            taskManagement.updateTaskStatus(taskId, status);

        } catch (NumberFormatException e) {

            System.out.println("ID phải là số!");

        } catch (IllegalArgumentException e) {

            System.out.println(e.getMessage());
        }
    }

    // ==========================
    // DELETE
    // ==========================

    public static void deleteTask() {

        try {

            System.out.print("Nhập ID công việc cần xóa: ");
            int taskId = Integer.parseInt(sc.nextLine());

            if (taskId <= 0) {
                throw new IllegalArgumentException(
                        "ID phải lớn hơn 0!"
                );
            }

            taskManagement.deleteTask(taskId);

        } catch (NumberFormatException e) {

            System.out.println("ID phải là số!");

        } catch (IllegalArgumentException e) {

            System.out.println(e.getMessage());
        }
    }

    // ==========================
    // SEARCH
    // ==========================

    public static void searchTask() {

        try {

            System.out.print("Nhập tên công việc cần tìm: ");
            String taskName = sc.nextLine().trim();

            if (taskName.isEmpty()) {
                throw new IllegalArgumentException(
                        "Tên tìm kiếm không được để trống!"
                );
            }

            taskManagement.searchTaskByName(taskName);

        } catch (IllegalArgumentException e) {

            System.out.println(e.getMessage());
        }
    }

    // ==========================
    // STATUS
    // ==========================

    public static String inputStatus() {

        while (true) {

            System.out.println("1. PENDING - Chưa hoàn thành");
            System.out.println("2. COMPLETED - Đã hoàn thành");
            System.out.print("Chọn trạng thái: ");

            String choice = sc.nextLine();

            if (choice.equals("1")) {
                return "PENDING";
            }

            if (choice.equals("2")) {
                return "COMPLETED";
            }

            System.out.println("Trạng thái không hợp lệ!");
        }
    }
}