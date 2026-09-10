package Bai5;

import Bai5.MovieManagement;
import Bai5.ConnectionDB;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        ConnectionDB.openConnection();

        Scanner sc = new Scanner(System.in);
        MovieManagement movieManagement = new MovieManagement();

        int choice;

        do {

            System.out.println("\n========== QUẢN LÝ PHIM ==========");
            System.out.println("1. Thêm phim");
            System.out.println("2. Liệt kê phim");
            System.out.println("3. Sửa phim");
            System.out.println("4. Xóa phim");
            System.out.println("0. Thoát");
            System.out.print("Nhập lựa chọn: ");

            try {

                choice = Integer.parseInt(sc.nextLine());

                switch (choice) {

                    case 1:

                        System.out.print("Nhập tên phim: ");
                        String title = sc.nextLine().trim();

                        if (title.isEmpty()) {
                            throw new IllegalArgumentException(
                                    "Tên phim không được để trống!"
                            );
                        }

                        System.out.print("Nhập đạo diễn: ");
                        String director = sc.nextLine().trim();

                        if (director.isEmpty()) {
                            throw new IllegalArgumentException(
                                    "Đạo diễn không được để trống!"
                            );
                        }

                        System.out.print("Nhập năm phát hành: ");
                        int year = Integer.parseInt(sc.nextLine());

                        if (year <= 0) {
                            throw new IllegalArgumentException(
                                    "Năm phát hành phải lớn hơn 0!"
                            );
                        }

                        movieManagement.addMovie(
                                title,
                                director,
                                year
                        );

                        break;

                    case 2:

                        movieManagement.listMovies();

                        break;

                    case 3:

                        System.out.print("Nhập ID phim cần sửa: ");
                        int updateId = Integer.parseInt(sc.nextLine());

                        System.out.print("Nhập tên phim mới: ");
                        String newTitle = sc.nextLine().trim();

                        if (newTitle.isEmpty()) {
                            throw new IllegalArgumentException(
                                    "Tên phim không được để trống!"
                            );
                        }

                        System.out.print("Nhập đạo diễn mới: ");
                        String newDirector = sc.nextLine().trim();

                        if (newDirector.isEmpty()) {
                            throw new IllegalArgumentException(
                                    "Đạo diễn không được để trống!"
                            );
                        }

                        System.out.print("Nhập năm phát hành mới: ");
                        int newYear = Integer.parseInt(sc.nextLine());

                        if (newYear <= 0) {
                            throw new IllegalArgumentException(
                                    "Năm phát hành phải lớn hơn 0!"
                            );
                        }

                        movieManagement.updateMovie(
                                updateId,
                                newTitle,
                                newDirector,
                                newYear
                        );

                        break;

                    case 4:

                        System.out.print("Nhập ID phim cần xóa: ");
                        int deleteId = Integer.parseInt(sc.nextLine());

                        movieManagement.deleteMovie(deleteId);

                        break;

                    case 0:

                        System.out.println("Đã thoát chương trình!");

                        break;

                    default:

                        System.out.println(
                                "Lựa chọn không hợp lệ!"
                        );
                }

            } catch (NumberFormatException e) {

                System.out.println(
                        "Vui lòng nhập đúng định dạng số!"
                );

                choice = -1;

            } catch (IllegalArgumentException e) {

                System.out.println(
                        "Dữ liệu không hợp lệ: "
                                + e.getMessage()
                );

                choice = -1;
            }

        } while (choice != 0);

        ConnectionDB.closeConnection();
        sc.close();
    }
}