package model;

public class Book extends Item {
    private String author;
    private int pageCount;
    private String isbn;

    public Book(String title, int yearPublished, String publisher, String author, int pageCount, String isbn) {
        super(title, yearPublished, publisher);
        this.author = author;
        this.pageCount = pageCount;
        this.isbn = isbn;
    }

    @Override
    public String getDescription() {
        return "Книга: " + getTitle() + ", автор: " + author;
    }
}

