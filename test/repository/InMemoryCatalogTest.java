package repository;

import exception.ItemNotFoundException;
import model.Book;
import model.Item;
import model.Magazine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryCatalogTest {

    private InMemoryCatalog catalog;

    @BeforeEach
    void setUp() {
        catalog = new InMemoryCatalog();
    }

    @Test
    void shouldAddItemAndAssignId() {
        Book book = new Book("Clean Code", 2008, "Prentice Hall",
                "Robert C. Martin", 464, "978-0-13-235088-4");

        catalog.add(book);

        assertEquals(1, catalog.size());
        assertEquals(1, book.getId());
        assertEquals(book, catalog.getById(1));
    }

    @Test
    void shouldAddMultipleItemsWithDifferentIds() {
        Book book1 = new Book("Clean Code", 2008, "Prentice Hall",
                "Robert C. Martin", 464, "978-0-13-235088-4");
        Book book2 = new Book("Effective Java", 2018, "Addison-Wesley",
                "Joshua Bloch", 416, "978-0-13-468599-1");

        catalog.add(book1);
        catalog.add(book2);

        assertEquals(2, catalog.size());
        assertEquals(1, book1.getId());
        assertEquals(2, book2.getId());
        assertEquals(book1, catalog.getById(1));
        assertEquals(book2, catalog.getById(2));
    }

    @Test
    void shouldThrowExceptionWhenAddingNullItem() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            catalog.add(null);
        });

        assertEquals("Cannot add null item", exception.getMessage());
    }


    @Test
    void shouldGetItemById() {
        Book book = new Book("Clean Code", 2008, "Prentice Hall",
                "Robert C. Martin", 464, "978-0-13-235088-4");
        catalog.add(book);

        Item found = catalog.getById(1);

        assertNotNull(found);
        assertEquals(book, found);
        assertEquals("Clean Code", found.getTitle());
    }

    @Test
    void shouldGetItemByIdWhenMultipleItemsExist() {
        Book book1 = new Book("Clean Code", 2008, "Prentice Hall",
                "Robert C. Martin", 464, "978-0-13-235088-4");
        Magazine magazine = new Magazine("National Geographic", 2023, "NG Media", 42, "Monthly");

        catalog.add(book1);
        catalog.add(magazine);

        Item found = catalog.getById(2);

        assertNotNull(found);
        assertEquals(magazine, found);
        assertInstanceOf(Magazine.class, found);
    }

    @Test
    void shouldThrowExceptionWhenItemNotFound() {
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, () -> {
            catalog.getById(999L);
        });

        assertEquals("Item with ID 999 not found", exception.getMessage());
    }


    @Test
    void shouldRemoveItemById() {
        Book book = new Book("Clean Code", 2008, "Prentice Hall",
                "Robert C. Martin", 464, "978-0-13-235088-4");
        catalog.add(book);

        catalog.remove(1);

        assertEquals(0, catalog.size());
        assertTrue(catalog.isEmpty());
        assertThrows(ItemNotFoundException.class, () -> catalog.getById(1));
    }

    @Test
    void shouldRemoveItemAndKeepOthers() {
        Book book1 = new Book("Clean Code", 2008, "Prentice Hall",
                "Robert C. Martin", 464, "978-0-13-235088-4");
        Book book2 = new Book("Effective Java", 2018, "Addison-Wesley",
                "Joshua Bloch", 416, "978-0-13-468599-1");

        catalog.add(book1);
        catalog.add(book2);

        catalog.remove(1);

        assertEquals(1, catalog.size());
        assertEquals(book2, catalog.getById(2));
        assertThrows(ItemNotFoundException.class, () -> catalog.getById(1));
    }

    @Test
    void shouldThrowExceptionWhenRemovingNonExistentItem() {
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, () -> {
            catalog.remove(999L);
        });

        assertEquals("Item with ID 999 not found", exception.getMessage());
    }


    @Test
    void shouldGetAllItems() {
        Book book1 = new Book("Clean Code", 2008, "Prentice Hall",
                "Robert C. Martin", 464, "978-0-13-235088-4");
        Book book2 = new Book("Effective Java", 2018, "Addison-Wesley",
                "Joshua Bloch", 416, "978-0-13-468599-1");
        Magazine magazine = new Magazine("National Geographic", 2023, "NG Media", 42, "Monthly");

        catalog.add(book1);
        catalog.add(book2);
        catalog.add(magazine);

        var items = catalog.getAll();

        assertEquals(3, items.size());
        assertTrue(items.contains(book1));
        assertTrue(items.contains(book2));
        assertTrue(items.contains(magazine));
    }

    @Test
    void shouldReturnEmptyListWhenCatalogIsEmpty() {
        var items = catalog.getAll();

        assertNotNull(items);
        assertTrue(items.isEmpty());
    }

    @Test
    void shouldReturnListInInsertionOrder() {
        Book book1 = new Book("Book1", 2020, "Pub1", "Author1", 100, "1");
        Book book2 = new Book("Book2", 2021, "Pub2", "Author2", 200, "2");
        Book book3 = new Book("Book3", 2022, "Pub3", "Author3", 300, "3");

        catalog.add(book1);
        catalog.add(book2);
        catalog.add(book3);

        var items = catalog.getAll();

        assertEquals(3, items.size());
        assertEquals(book1, items.get(0));
        assertEquals(book2, items.get(1));
        assertEquals(book3, items.get(2));
    }

    @Test
    void shouldClearCatalog() {
        Book book1 = new Book("Clean Code", 2008, "Prentice Hall",
                "Robert C. Martin", 464, "978-0-13-235088-4");
        Book book2 = new Book("Effective Java", 2018, "Addison-Wesley",
                "Joshua Bloch", 416, "978-0-13-468599-1");

        catalog.add(book1);
        catalog.add(book2);

        catalog.clear();

        assertEquals(0, catalog.size());
        assertTrue(catalog.isEmpty());
        assertEquals(1, catalog.getCurrentId());
    }

    @Test
    void shouldResetCurrentIdAfterClear() {
        Book book = new Book("Clean Code", 2008, "Prentice Hall",
                "Robert C. Martin", 464, "978-0-13-235088-4");

        catalog.add(book);
        assertEquals(2, catalog.getCurrentId()); // После добавления currentId = 2

        catalog.clear();

        assertEquals(1, catalog.getCurrentId());
    }

    @Test
    void shouldReturnCorrectSize() {
        Book book1 = new Book("Clean Code", 2008, "Prentice Hall",
                "Robert C. Martin", 464, "978-0-13-235088-4");
        Book book2 = new Book("Effective Java", 2018, "Addison-Wesley",
                "Joshua Bloch", 416, "978-0-13-468599-1");

        catalog.add(book1);

        assertEquals(1, catalog.size());
        assertFalse(catalog.isEmpty());

        catalog.add(book2);

        assertEquals(2, catalog.size());
        assertFalse(catalog.isEmpty());

        catalog.remove(1);

        assertEquals(1, catalog.size());
        assertFalse(catalog.isEmpty());

        catalog.remove(2);

        assertEquals(0, catalog.size());
        assertTrue(catalog.isEmpty());
    }

    @Test
    void shouldReturnStorageMap() {
        Book book = new Book("Clean Code", 2008, "Prentice Hall",
                "Robert C. Martin", 464, "978-0-13-235088-4");
        catalog.add(book);

        var storage = catalog.getStorage();

        assertNotNull(storage);
        assertEquals(1, storage.size());
        assertTrue(storage.containsKey(1L));
        assertEquals(book, storage.get(1L));
    }

    @Test
    void shouldReturnModifiableStorage() {
        Book book = new Book("Clean Code", 2008, "Prentice Hall",
                "Robert C. Martin", 464, "978-0-13-235088-4");
        catalog.add(book);

        var storage = catalog.getStorage();
        storage.remove(1L);

        assertEquals(0, storage.size());
        assertEquals(0, catalog.size()); // Изменения видны и в каталоге
    }
}