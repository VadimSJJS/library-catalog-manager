package model;

import exception.InvalidDataException;

public class Book extends Item {
    private String author;
    private int pageCount;
    private String isbn;

    public Book(String title, int yearPublished, String publisher, String author, int pageCount, String isbn) {
        super(title, yearPublished, publisher);

        if (pageCount <= 0) {
            throw new InvalidDataException("Количество страниц должно быть больше 0");
        }

        this.author = author;
        this.pageCount = pageCount;
        this.isbn = isbn;
    }

    @Override
    public String getDescription() {
        return "Книга: " + getTitle() + ", автор: " + author;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", yearPublished=" + getYearPublished() +
                ", publisher='" + getPublisher() + '\'' +
                ", author='" + author + '\'' +
                ", pageCount=" + pageCount +
                ", isbn='" + isbn + '\'' +
                '}';
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
