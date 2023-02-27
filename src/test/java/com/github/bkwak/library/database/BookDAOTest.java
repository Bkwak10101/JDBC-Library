package com.github.bkwak.library.database;

import com.github.bkwak.library.model.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class BookDAOTest {
    private final BookDAO bookDB = BookDAO.getInstance();

    @Test
    @DisplayName("should rent a book successfully")
    public void RentBook_bookIsNotRented() {
        List<String> bookInfo = new ArrayList<>();
        bookInfo.add("Mamba mentality: How I play");
        bookInfo.add("Janusz");
        bookInfo.add("Nowak");
        assertTrue(bookDB.rentBook(bookInfo));
    }

    @Test
    @DisplayName("should not rent a book which is rented already")
    public void RentBook_bookIsRented() {
        List<String> bookInfo = new ArrayList<>();
        bookInfo.add("Mamba mentality: How I play");
        bookInfo.add("Anna");
        bookInfo.add("Nowak");
        assertFalse(bookDB.rentBook(bookInfo));
    }

    @Test
    @DisplayName("searchWith")
    public void search() {
        String input = "Wiedzmin";
        String expectedTitle = "Miecz przeznaczenia. Wiedzmin";
        String expectedTitle2 = "Czas Pogardy. Wiedzmin";
        Book book1 = new Book("Miecz przeznaczenia. Wiedzmin", "Andrzej Sapkowski", "9788375780642",
                Book.Category.FANTASY, false);
        Book book2 = new Book("Czas Pogardy. Wiedzmin", "Andrzej Sapkowski", "9788375780666",
                Book.Category.FANTASY, false);
        Map<List<String>, Book> expected = new HashMap<>();
        ArrayList<String> reservation1 = new ArrayList<>();
        reservation1.add(null);
        reservation1.add(null);
        reservation1.add(null);
        reservation1.add(null);
        reservation1.add(expectedTitle);
        expected.put(reservation1, book1);
        ArrayList<String> reservation2 = new ArrayList<>();
        reservation2.add(null);
        reservation2.add(null);
        reservation2.add(null);
        reservation2.add(null);
        reservation2.add(expectedTitle2);
        expected.put(reservation2, book2);
        System.out.println("EXPECTED");

        for (Map.Entry<List<String>, Book> entry : expected.entrySet()) {
            List<String> reservation3 = entry.getKey();
            Book book = entry.getValue();
            System.out.println("Reservation: " + reservation3);
            System.out.println("Book: " + book);
        }

        System.out.println("RESULT");
        Map<List<String>, Book> result = bookDB.search(input);
        for (Map.Entry<List<String>, Book> entry : result.entrySet()) {
            List<String> reservation4 = entry.getKey();
            Book book = entry.getValue();
            System.out.println("Reservation: " + reservation4);
            System.out.println("Book: " + book);
        }
        assertEquals(expected, result);
    }
}
