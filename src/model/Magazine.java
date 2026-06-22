package model;

public class Magazine extends Item {
    private int issueNumber;
    private String frequency;

    public Magazine(String title, int yearPublished, String publisher, int issueNumber, String frequency) {
        super(title, yearPublished, publisher);
        this.issueNumber = issueNumber;
        this.frequency = frequency;
    }

    @Override
    public String getDescription() {
        return "Журнал: " + getTitle() + ", выпуск №" + issueNumber;
    }
}
