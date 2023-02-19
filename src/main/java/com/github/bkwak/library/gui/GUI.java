package com.github.bkwak.library.gui;

import com.github.bkwak.library.core.Authenticator;
import com.github.bkwak.library.database.BookDAO;
import com.github.bkwak.library.database.UserDAO;
import com.github.bkwak.library.model.Book;
import com.github.bkwak.library.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class GUI {
    private static final GUI instance = new GUI();
    final Authenticator authenticator = Authenticator.getInstance();
    final BookDAO bookDB = BookDAO.getInstance();
    final UserDAO userDB = UserDAO.getInstance();
    private final Scanner scanner = new Scanner(System.in);

    private GUI() {
    }

    public static GUI getInstance() {
        return instance;
    }

    public static void showSignUpResult(boolean result) {
        if (result) {
            System.out.println("\nRegistration successful\n");
        } else {
            System.out.println("Login is already taken");
        }
    }

    public String showStartMenu() {
        System.out.println("1. Sign in");
//        System.out.println("2. Sign up");
        System.out.println("2. Exit");
        return this.scanner.nextLine();
    }

    public String showMenu() {
        System.out.println("1. List books");
        System.out.println("2. List rented books");
        System.out.println("3. List overdue books");
        System.out.println("4. Rent books");
        System.out.println("5. Search books");
        System.out.println("6. Sign out");
        System.out.println("7. Exit");
        if (authenticator.getLoggedUser() != null &&
                authenticator.getLoggedUser().getRole() == User.Role.ADMIN) {
            System.out.println("8. Add book");
        }
        return this.scanner.nextLine();
    }

    public User readLoginAndPassword() {
        User user = new User();
        System.out.println("Login:");
        user.setLogin(this.scanner.nextLine());
        System.out.println("Password:");
        user.setPassword(this.scanner.nextLine());
        return user;
    }

    public void searchBooks(){

    }

//    public User setNewUser() {
//        User user = new User();
//        System.out.println("Login:");
//        user.setLogin(this.scanner.nextLine());
//        System.out.println("Password:");
//        user.setPassword(DigestUtils.md5Hex(this.scanner.nextLine() + Authenticator.seed));
//        return user;
//    }

    public void listBooks() {
        for (Book book : this.bookDB.getBooks()) {
            System.out.println(book);
        }
    }

    public void listRentedBooks(){
        Map<List<String>, Book> rentedBooks = this.bookDB.getRentedBooks();
        rentedBooks.forEach((user, book) -> {
            System.out.println("--------------------------------------");
            System.out.println("User: " + user.get(0) + " " + user.get(1));
            System.out.println(book);
        });

    }

    public void listOverdueBooks(){
        Map<List<String>, Book> overdueBooks = this.bookDB.getOverdueBooks();
        overdueBooks.forEach((user, book) -> {
            System.out.println("--------------------------------------");
            System.out.println("User: " + user.get(0) + " " + user.get(1));
            System.out.println(book);
        });
    }

    public List<String> readReservation() {
        ArrayList<String> info = new ArrayList<>();
        System.out.println("Title:");
        String title = this.scanner.nextLine();
        info.add(title);
        for (Book book : this.bookDB.getBooks()) {
            if(title.equals(book.getTitle())){
                info.add(book.getIsbn());
            }
        }
        System.out.println("Name: ");
        info.add(this.scanner.nextLine());
        System.out.println("Surname: ");
        info.add(this.scanner.nextLine());
        return info;
    }

    public void showRentResult(boolean result) {
        if (result) {
            System.out.println("Rent successfull");
        } else {
            System.out.println("Title does not exist or book is already rent");
        }
    }

    public Book readNewBookData() {
        System.out.println("1. Biography");
        System.out.println("2. Crime");
        System.out.println("3. Thriller");
        System.out.println("4. Fantasy");
        System.out.println("5. Science-fiction");
        String category = this.scanner.nextLine();
        System.out.println("Title:");
        String title = this.scanner.nextLine();
        System.out.println("Author:");
        String author = this.scanner.nextLine();
        System.out.println("Isbn:");
        String isbn = this.scanner.nextLine();

        return switch (category) {
            case "1" -> new Book(title, author, isbn, Book.Category.BIOGRAPHY, false);
            case "2" -> new Book(title, author, isbn, Book.Category.CRIME, false);
            case "3" -> new Book(title, author, isbn, Book.Category.THRILLER, false);
            case "4" -> new Book(title, author, isbn, Book.Category.FANTASY, false);
            case "5" -> new Book(title, author, isbn, Book.Category.SCIENCE_FICTION, false);
            default -> null;
        };
    }
}
