import exception.InvalidDataException;
import model.Book;
import model.Item;
import model.Magazine;
import service.LibraryCatalog;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Item book = new Book("Book 1", 2003, "Publisher 1", "Author 1", 2, "isbn 1");
        Item magazine = new Magazine("Magazine 1", 2003, "Publisher 1", 1, "frequency 1");
        Item book2 = new Book("Book 2", 2003, "Publisher 1", "Author 1", 2, "isbn 1");
        Item magazine2 = new Magazine("Magazine 2", 2003, "Publisher 1", 1, "frequency 1");
        Item book3 = new Book("Book 3", 2003, "Publisher 1", "Author 1", 2, "isbn 1");
        Item magazine3 = new Magazine("Magazine 3", 2003, "Publisher 1", 1, "frequency 1");

        System.out.println(book.getDescription());
        System.out.println(magazine.getDescription());

        try {
            Item wrongBook = new Book("Book 1", 2003, "Publisher 1", "Author 1", -2, "isbn 1");
        } catch (InvalidDataException ex) {
            System.out.println("Ошибка: " + ex.getMessage());
        }

        LibraryCatalog libraryCatalog = new LibraryCatalog();

        libraryCatalog.addItem(book);
        libraryCatalog.addItem(magazine);
        libraryCatalog.addItem(book2);
        libraryCatalog.addItem(magazine2);
        libraryCatalog.addItem(book3);
        libraryCatalog.addItem(magazine3);

        List<Item> items = libraryCatalog.getAllItems();
        System.out.println(items);

        libraryCatalog.removeItem(2);
        List<Item> items2 = libraryCatalog.getAllItems();

        System.out.println(items2);

        System.out.println(magazine instanceof Book);
        System.out.println(book2 instanceof Book);
    }
}
