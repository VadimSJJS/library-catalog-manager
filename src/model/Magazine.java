package model;

import exception.InvalidDataException;

public class Magazine extends Item {
    private int issueNumber;
    private String frequency;

    public Magazine(String title, int yearPublished, String publisher, int issueNumber, String frequency) {
        super(title, yearPublished, publisher);

        if (issueNumber <= 0 ) {
            throw new InvalidDataException("The issue number cannot be <= 0");
        }

        this.issueNumber = issueNumber;
        this.frequency = frequency;
    }

    @Override
    public String getDescription() {
        return "Magazine: " + getTitle() + ", issue number №" + issueNumber;
    }

    public int getIssueNumber() {
        return issueNumber;
    }

    public void setIssueNumber(int issueNumber) {
        this.issueNumber = issueNumber;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return "Magazine{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", yearPublished=" + getYearPublished() +
                ", publisher='" + getPublisher() + '\'' +
                ", issueNumber=" + issueNumber +
                ", frequency='" + frequency + '\'' +
                '}';
    }
}
