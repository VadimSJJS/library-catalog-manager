
package model;

public abstract class Item {
    private long id;
    private String title;
    private int yearPublished;
    private String publisher;

    public abstract String getDescription();

    public Item(String title, int yearPublished, String publisher) {
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
}
