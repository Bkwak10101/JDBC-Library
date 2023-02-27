package com.github.bkwak.library.core;

import com.github.bkwak.library.database.BookDAO;
import com.github.bkwak.library.gui.GUI;
import com.github.bkwak.library.model.User;

public class Core {
    private static final Core instance = new Core();
    final BookDAO bookDB = BookDAO.getInstance();
    final Authenticator authenticator = Authenticator.getInstance();
    final GUI gui = GUI.getInstance();

    private Core() {
    }

    public static Core getInstance() {
        return instance;
    }

    public void start() {
        boolean isRunning = false;
        while (!isRunning) {
            switch (gui.showStartMenu()) {
                case "1":
                    isRunning = signIn(isRunning);
                    break;
                case "2":
                    System.exit(0);
                    break;
            }
        }
        menu();
    }

    public void menu() {
        while (true) {
            switch (gui.showMenu()) {
                case "1":
                    gui.listBooks();
                    break;
                case "2":
                    gui.listRentedBooks();
                    break;
                case "3":
                    gui.listOverdueBooks();
                    break;
                case "4":
                    gui.showRentResult((bookDB.rentBook(gui.readReservation())));
                    break;
                case "5":
                    gui.listSearchResult();
                    break;
                case "6":
                    authenticator.setLoggedUser(null);
                    start();
                case "7":
                    System.exit(0);
                    break;
                case "8":
                    if (this.authenticator.getLoggedUser().getRole() == User.Role.ADMIN) {
                        this.bookDB.addBook(this.gui.readNewBookData());
                        break;
                    }
                    break;
                default:
                    System.out.println("Wrong choice!!!");
                    break;
            }
        }
    }

    public boolean signIn(boolean isRunning) {
        int counter = 0;
        while (!isRunning && counter < 3) {
            this.authenticator.authenticate(this.gui.readLoginAndPassword());
            isRunning = authenticator.getLoggedUser() != null;
            if (!isRunning) {
                System.out.println("Not authorized !!!");
            }
            counter++;
        }
        return isRunning;
    }
}
