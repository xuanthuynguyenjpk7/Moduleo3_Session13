package Bai5;

import Bai5.ConnectionDB;
import Bai5.Movie;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MovieManagement {

    // 1. Thêm phim
    public void addMovie(String title, String director, int year) {

        String sql = "{CALL add_movie(?,?,?)}";

        try (CallableStatement stmt = ConnectionDB.conn.prepareCall(sql)) {

            stmt.setString(1, title);
            stmt.setString(2, director);
            stmt.setInt(3, year);

            stmt.execute();

            System.out.println("Thêm phim thành công!");

        } catch (SQLException e) {
            System.out.println("Lỗi khi thêm phim!");
            e.printStackTrace();
        }
    }

    // 2. Liệt kê phim
    public void listMovies() {

        String sql = "{CALL list_movies()}";

        try (CallableStatement stmt = ConnectionDB.conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("\n===== DANH SÁCH PHIM =====");

            boolean hasMovie = false;

            while (rs.next()) {

                hasMovie = true;

                int id = rs.getInt("id");
                String title = rs.getString("title");
                String director = rs.getString("director");
                int year = rs.getInt("year");

                Movie movie = new Movie(
                        id,
                        title,
                        director,
                        year
                );

                System.out.println(movie);
            }

            if (!hasMovie) {
                System.out.println("Chưa có phim nào!");
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy danh sách phim!");
            e.printStackTrace();
        }
    }

    // 3. Sửa phim
    public void updateMovie(
            int id,
            String title,
            String director,
            int year) {

        String sql = "{CALL update_movie(?,?,?,?)}";

        try (CallableStatement stmt = ConnectionDB.conn.prepareCall(sql)) {

            stmt.setInt(1, id);
            stmt.setString(2, title);
            stmt.setString(3, director);
            stmt.setInt(4, year);

            int result = stmt.executeUpdate();

            if (result > 0) {
                System.out.println("Cập nhật phim thành công!");
            } else {
                System.out.println("Không tìm thấy phim có ID = " + id);
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi cập nhật phim!");
            e.printStackTrace();
        }
    }

    // 4. Xóa phim
    public void deleteMovie(int id) {

        String sql = "{CALL delete_movie(?)}";

        try (CallableStatement stmt = ConnectionDB.conn.prepareCall(sql)) {

            stmt.setInt(1, id);

            int result = stmt.executeUpdate();

            if (result > 0) {
                System.out.println("Xóa phim thành công!");
            } else {
                System.out.println("Không tìm thấy phim có ID = " + id);
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi xóa phim!");
            e.printStackTrace();
        }
    }
}