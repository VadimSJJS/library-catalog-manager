package service;

import exception.InvalidDataException;
import exception.ItemNotFoundException;
import model.Book;
import model.Item;
import model.Magazine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.InMemoryCatalog;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CatalogServiceTest {

    private LibraryCatalogService service;
    private InMemoryCatalog catalog;

    @BeforeEach
    void setUp() {
        catalog = new InMemoryCatalog();
        service = new LibraryCatalogService(catalog);
    }

    @Test
    void shouldAddBookToCatalog() {
        // given
        Book book = new Book("Clean Code", 2008, "Prentice Hall",
                "Robert C. Martin", 464, "978-0-13-235088-4");

        service.addItem(book);

        List<Item> items = service.getAllItems();
        assertEquals(1, items.size());
        assertEquals(book, items.get(0));
        assertEquals(1, book.getId());
    }

    @Test
    void shouldAddMagazineToCatalog() {
        Magazine magazine = new Magazine("National Geographic", 2023, "NG Media",
                42, "Monthly");

        service.addItem(magazine);

        List<Item> items = service.getAllItems();
        assertEquals(1, items.size());
        assertEquals(magazine, items.get(0));
        assertEquals(1, magazine.getId());
    }

    @Test
    void shouldAddMultipleItemsWithDifferentIds() {
        Book book = new Book("Clean Code", 2008, "Prentice Hall",
                "Robert C. Martin", 464, "978-0-13-235088-4");
        Magazine magazine = new Magazine("National Geographic", 2023, "NG Media",
                42, "Monthly");

        service.addItem(book);
        service.addItem(magazine);

        List<Item> items = service.getAllItems();
        assertEquals(2, items.size());
        assertEquals(1, book.getId());
        assertEquals(2, magazine.getId());
    }

    @Test
    void shouldThrowExceptionWhenAddingNullItem() {
        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> service.addItem(null));

        assertEquals("Cannot add null item to catalog", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAddingInvalidData() {
        assertThrows(InvalidDataException.class, () -> {
            service.addItem(new Book("Invalid", -2020, "Pub", "Author", -10, "123"));
        });
    }

    @Test
    void shouldGetItemById() {
        Book book = new Book("Clean Code", 2008, "Prentice Hall",
                "Robert C. Martin", 464, "978-0-13-235088-4");
        service.addItem(book);
        long id = book.getId();

        Item found = service.getItemById(id);

        assertEquals(book, found);
    }

    @Test
    void shouldThrowExceptionWhenItemNotFoundById() {
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class,
                () -> service.getItemById(999L));

        assertEquals("Item with ID 999 not found", exception.getMessage());
    }

    @Test
    void shouldRemoveItemById() {
        Book book = new Book("Clean Code", 2008, "Prentice Hall",
                "Robert C. Martin", 464, "978-0-13-235088-4");
        service.addItem(book);
        long id = book.getId();

        service.removeItem(id);

        List<Item> items = service.getAllItems();
        assertEquals(0, items.size());
        assertThrows(ItemNotFoundException.class, () -> service.getItemById(id));
    }

    @Test
    void shouldRemoveItemByIdAndKeepOthers() {
        // given
        Book book1 = new Book("Clean Code", 2008, "Prentice Hall",
                "Robert C. Martin", 464, "978-0-13-235088-4");
        Book book2 = new Book("Effective Java", 2018, "Addison-Wesley",
                "Joshua Bloch", 416, "978-0-13-468599-1");
        service.addItem(book1);
        service.addItem(book2);

        service.removeItem(1L);

        List<Item> items = service.getAllItems();
        assertEquals(1, items.size());
        assertEquals(book2, items.get(0));
        assertThrows(ItemNotFoundException.class, () -> service.getItemById(1L));
    }

    @Test
    void shouldThrowExceptionWhenRemovingNonExistentItem() {
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class,
                () -> service.removeItem(999L));

        assertEquals("Item with ID 999 not found", exception.getMessage());
    }
}