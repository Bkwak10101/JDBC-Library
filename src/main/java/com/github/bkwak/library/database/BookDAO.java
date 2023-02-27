package com.github.bkwak.library.database;

import com.github.bkwak.library.model.Book;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<List<String>, Book> getRentedBooks() {
        Map<List<String>, Book> result = new HashMap<>();
        try {
            String sql =
                    "SELECT b.title, b.author, b.isbn, b.category, b.rent, r.name, r.surname\n" +
                            "FROM book AS b\n" +
                            "JOIN reservation AS r ON b.book_id = r.book_id\n" +
                            "WHERE b.rent = 1";
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String title = rs.getString("title");
                String author = rs.getString("author");
                String isbn = rs.getString("isbn");
                Book.Category category = Book.Category.valueOf(rs.getString("category"));
                boolean rent = rs.getBoolean("rent");
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                ArrayList<String> reservation = new ArrayList<>();
                reservation.add(name);
                reservation.add(surname);
                reservation.add(title);
                result.put(reservation, new Book(title, author, isbn, category, rent));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return result;
    }

    public Map<List<String>, Book> getOverdueBooks() {
        Map<List<String>, Book> result = new HashMap<>();
        try {
            String sql =
                    "SELECT b.title, b.author, b.isbn, b.category, b.rent, r.name, r.surname, r.out_date, r.due_date\n" +
                            "FROM book AS b\n" +
                            "JOIN reservation AS r ON b.book_id = r.book_id\n" +
                            "WHERE (r.due_date < ? AND b.rent = 1)";
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.setString(1, getLocalTime());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String title = rs.getString("title");
                String author = rs.getString("author");
                String isbn = rs.getString("isbn");
                Book.Category category = Book.Category.valueOf(rs.getString("category"));
                boolean rent = rs.getBoolean("rent");
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                ArrayList<String> reservation = new ArrayList<>();
                reservation.add(name);
                reservation.add(surname);
                reservation.add(title);
                result.put(reservation, new Book(title, author, isbn, category, rent));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return result;
    }

    private String getLocalTime() {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
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

    public boolean rentBook(List<String> info) {
        String sql = "SELECT * FROM book WHERE title = ?";
        try {
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.setString(1, info.get(0));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                boolean rent = rs.getBoolean("rent");
                if (!rent) {
                    String updateSql = "UPDATE book SET rent = ? WHERE book_id = ?";
                    int bookId = rs.getInt("book_id");
                    PreparedStatement updatePs = this.connection.prepareStatement(updateSql);
                    updatePs.setBoolean(1, true);
                    updatePs.setInt(2, bookId);
                    updatePs.executeUpdate();
                    String updateSql2 = "INSERT INTO reservation " +
                            "(book_id, title, name, surname, out_date, due_date) VALUES (?,?,?,?,?,?)";
                    LocalDateTime dateTime = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    PreparedStatement updatePs2 = this.connection.prepareStatement(updateSql2);
                    updatePs2.setInt(1, bookId);
                    updatePs2.setString(2, info.get(0));
                    updatePs2.setString(3, info.get(1));
                    updatePs2.setString(4, info.get(2));
                    updatePs2.setString(5, getLocalTime());
                    updatePs2.setString(6, dateTime.plus(Duration.ofDays(14)).format(formatter));
                    updatePs2.executeUpdate();
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public Map<List<String>, Book> search(String input) {
        Map<List<String>, Book> result = new HashMap<>();
        try {
            String sql =
                    "SELECT b.title, b.author, b.isbn, b.category, b.rent, r.name, r.surname, r.out_date, r.due_date " +
                            "FROM book b LEFT JOIN reservation r ON b.book_id = r.book_id " +
                            "WHERE b.title LIKE ? OR b.author LIKE ? OR b.isbn LIKE ?";
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.setString(1, "%" + input + "%");
            ps.setString(2, "%" + input + "%");
            ps.setString(3, "%" + input + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String title = rs.getString("title");
                String author = rs.getString("author");
                String isbn = rs.getString("isbn");
                Book.Category category = Book.Category.valueOf(rs.getString("category"));
                boolean rent = rs.getBoolean("rent");
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                String out_date = rs.getString("out_date");
                String due_date = rs.getString("due_date");
                ArrayList<String> reservation = new ArrayList<>();
                reservation.add(name);
                reservation.add(surname);
                reservation.add(out_date);
                reservation.add(due_date);
                reservation.add(title);
                result.put(reservation, new Book(title, author, isbn, category, rent));
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return result;
    }

}