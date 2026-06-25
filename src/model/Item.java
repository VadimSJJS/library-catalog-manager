
package model;

import exception.InvalidDataException;

import java.time.LocalDateTime;

public abstract class Item {
    private long id;
    private String title;
    private int yearPublished;
    private String publisher;

    public abstract String getDescription();

    public Item(String title, int yearPublished, String publisher) {
        if (yearPublished < 0 || yearPublished > LocalDateTime.now().getYear()) {
            throw new InvalidDataException("The publication year cannot be less than 0 or greater than the current year.");
        }

        this.title = title;
        this.yearPublished = yearPublished;
        this.publisher = publisher;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYearPublished() {
        return yearPublished;
    }

    public void setYearPublished(int yearPublished) {
        this.yearPublished = yearPublished;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", yearPublished=" + yearPublished +
                ", publisher='" + publisher + '\'' +
                '}';
    }
}
