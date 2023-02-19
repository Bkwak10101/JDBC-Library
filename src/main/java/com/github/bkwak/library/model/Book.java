package com.github.bkwak.library.model;

public class Book {
    private String author;
    private String title;
    private String isbn;
    private Category category;
    private boolean rent;

    public Book(String title, String author, String isbn, Category category, boolean rent) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.category = category;
        this.rent = rent;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public boolean isRent() {
        return rent;
    }

    public void setRent(boolean rent) {
        this.rent = rent;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("--------------------------------------")
                .append("\n Title: ").append((this.getTitle()))
                .append("\n Author: ").append((this.getAuthor()))
                .append("\n Category: ").append(categoryName(this.getCategory()))
                .toString();
    }

    private String categoryName(Category category) {
        return switch (category) {
            case BIOGRAPHY -> "Biography";
            case CRIME -> "Crime";
            case FANTASY -> "Fantasy";
            case THRILLER -> "Thriller";
            case SCIENCE_FICTION -> "Science Fiction";
        };
    }

    public enum Category {
        BIOGRAPHY,
        CRIME,
        THRILLER,
        FANTASY,
        SCIENCE_FICTION
    }

}
