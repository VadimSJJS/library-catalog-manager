import exception.InvalidDataException;
import exception.ItemNotFoundException;
import model.Book;
import model.Item;
import model.Magazine;
import service.ItemFilter;
import service.LibraryCatalog;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Item book1 = new Book("Book 1", 123, "Publisher 1", "Author 1", 23, "isbn1");
        Item book2 = new Book("Book 2", 123, "Publisher 1", "Author 1", 23, "isbn1");
        Item magazine1 = new Magazine("Magazine 1", 123, "Publisher 1", 1, "frequency");
        Item magazine2 = new Magazine("Magazine 2", 123, "Publisher 1", 1, "frequency");

        LibraryCatalog libraryCatalog = new LibraryCatalog();
        libraryCatalog.addItem(book1);
        libraryCatalog.addItem(book2);
        libraryCatalog.addItem(magazine1);
        libraryCatalog.addItem(magazine2);

        List<Item> items = libraryCatalog.getAllItems();
        System.out.println(items);

        libraryCatalog.removeItem(1);
        items = libraryCatalog.getAllItems();
        System.out.println(items);

        libraryCatalog.removeItem(1);
        items = libraryCatalog.getAllItems();
        System.out.println(items);
    }
}
