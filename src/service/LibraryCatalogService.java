package service;

import exception.InvalidDataException;
import model.Book;
import model.Item;
import repository.InMemoryCatalog;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LibraryCatalogService {
    private final InMemoryCatalog catalog;

    public LibraryCatalogService(InMemoryCatalog catalog) {
        this.catalog = catalog;
    }

    public void addItem(Item item) {
        if (item == null) {
            throw new InvalidDataException("Cannot add null item to catalog");
        }
        catalog.add(item);
    }

    public Item getItemById(long id) {
        return catalog.getById(id);
    }

    public void removeItem(long id) {
        catalog.remove(id);
    }

    public List<Item> getAllItems() {
        return catalog.getAll();
    }

    public void clear() {
        catalog.clear();
    }

    public List<Item> searchByTitleKeyword(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            throw new InvalidDataException("Keyword cannot be null or empty");
        }

        return catalog.getAll().stream()
                .filter(item -> item.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Book> searchBooksByAuthor(String author) {
        if (author == null || author.isEmpty()) {
            throw new InvalidDataException("Author name cannot be null or empty");
        }

        return catalog.getAll().stream()
                .filter(item -> item instanceof Book)
                .map(item -> (Book) item)
                .filter(book -> book.getAuthor().equalsIgnoreCase(author))
                .collect(Collectors.toList());
    }

    public List<Item> findItemsPublishedAfter(int year) {
        if (year < 0) {
            throw new InvalidDataException("Year cannot be negative");
        }

        return catalog.getAll().stream()
                .filter(item -> item.getYearPublished() >= year)
                .collect(Collectors.toList());
    }

    public Map<String, Long> getTypeStatistics() {
        return catalog.getAll().stream()
                .collect(Collectors.groupingBy(
                        item -> item instanceof Book ? "Book" : "Magazine",
                        Collectors.counting()
                ));
    }

    public Map<Long, Item> getStorage() {
        return catalog.getStorage();
    }

    public void setCurrentId(long currentId) {
        catalog.setCurrentId(currentId);
    }

    public long getCurrentId() {
        return catalog.getCurrentId();
    }

    public void restoreFromStorage(Map<Long, Item> storage) {
        catalog.clear();
        storage.forEach((id, item) -> {
            item.setId(id);
            catalog.getStorage().put(id, item);
        });
        long maxId = storage.keySet().stream().max(Long::compareTo).orElse(0L);
        catalog.setCurrentId(maxId + 1);
    }
}