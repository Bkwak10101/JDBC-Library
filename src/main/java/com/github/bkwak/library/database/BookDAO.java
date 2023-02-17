package com.github.bkwak.library.database;

import com.github.bkwak.library.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    private static final BookDAO instance = new BookDAO();
    private final Connection connection;

    private BookDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/library",
                    "root",
                    "");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static BookDAO getInstance() {
        return instance;
    }

    public List<Book> getBooks() {
        ArrayList<Book> result = new ArrayList<>();

        try {
            String sql = "SELECT * FROM book";
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                String title = rs.getString("title");
                String author = rs.getString("author");
                String isbn = rs.getString("isbn");
                Book.Category category = Book.Category.valueOf(rs.getString("category"));
                boolean rent = rs.getBoolean("rent");
                result.add(new Book(title, author, isbn, category, rent));

            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return result;
    }

    public List<Book> getRentedBooks() {
        ArrayList<Book> result = new ArrayList<>();

        try {
            String sql = "SELECT * FROM book WHERE rent = 1";
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                String title = rs.getString("title");
                String author = rs.getString("author");
                String isbn = rs.getString("isbn");
                Book.Category type = Book.Category.valueOf(rs.getString("category"));
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return result;
    }

    public void addBook(Book book) {
        Book.Category category = switch (book.getCategory()) {
            case BIOGRAPHY -> Book.Category.BIOGRAPHY;
            case CRIME -> Book.Category.CRIME;
            case THRILLER -> Book.Category.THRILLER;
            case FANTASY -> Book.Category.FANTASY;
            case SCIENCE_FICTION -> Book.Category.SCIENCE_FICTION;
        };

        try {
            String sql = "INSERT INTO book " +
                    "(title, author, isbn, category, rent) VALUES (?,?,?,?,?)";
            PreparedStatement ps = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.setString(4, category.toString());
            ps.setBoolean(5, book.isRent());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean rentBook(String plate) {
        String sql = "SELECT * FROM book WHERE author = ?";
        try {
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.setString(1, plate);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                boolean rent = rs.getBoolean("rent");
                if (!rent) {
                    String updateSql = "UPDATE tvehicle SET rent = ? WHERE id = ?";
                    int vehicleId = rs.getInt("id");

                    PreparedStatement updatePs = this.connection.prepareStatement(updateSql);
                    updatePs.setBoolean(1, true);
                    updatePs.setInt(2, vehicleId);
                    updatePs.executeUpdate();
                    return true;
                }
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
