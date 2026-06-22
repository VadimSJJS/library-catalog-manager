package service;

import exception.ItemNotFoundException;
import model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            throw new ItemNotFoundException("Элемент с id " + id + " не найден")
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
}
