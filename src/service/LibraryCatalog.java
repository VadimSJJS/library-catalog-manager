package service;

import exception.InvalidDataException;
import exception.ItemNotFoundException;
import model.Book;
import model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LibraryCatalog {
    private final Map<Long, Item> storage = new HashMap<>(); // {id:item}
    private long currentId = 1;

    public void addItem(Item item) {
        item.setId(currentId++);
        storage.put(item.getId(), item);
    }

    public Item getItemById(long id) {
        Item item = storage.get(id);

        if (item == null) {
            throw new ItemNotFoundException("Элемент с id " + id + " не найден");
        }

        return item;
    }

    public void removeItem(long id) {
        getItemById(id); // проверка на существование элемента
        storage.remove(id);
    }

    public List<Item> getAllItems() {
        return new ArrayList<>(storage.values());
    }

    /*
    STREAM API
    */

    public List<Item> searchByTitleKeyword(String keyword) {
        return storage.values().stream()
            .filter(item -> item.getTitle().toLowerCase().contains(keyword.toLowerCase()))
            .toList();
    }

    public List<Book> searchBooksByAuthor(String author) {
        return storage.values().stream()
            .filter(item -> item instanceof Book)
            .map(item -> (Book)item)
            .filter(book -> book.getAuthor().equalsIgnoreCase(author))
            .toList();
    }

    public List<Item> findItemsPublishedAfter(int year) throws InvalidDataException {
        return storage.values().stream()
            .filter(item -> item.getYearPublished() >= year)
            .toList();
    }

    // возращает кол-во объектов каждого типа
    public Map<String, Long> getTypeStatistics() {
        return storage.values().stream()
            .collect(Collectors.groupingBy(
                item -> item instanceof Book ? "Book" : "Magazine",
                Collectors.counting()
            ));
    }

    public List<Item> filterItems(ItemFilter filter) {
        return storage.values().stream()
            .filter(filter::test)
            .collect(Collectors.toList());
    }
}
