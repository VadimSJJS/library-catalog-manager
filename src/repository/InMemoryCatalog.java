package repository;

import exception.ItemNotFoundException;
import model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryCatalog {
    private final Map<Long, Item> storage = new HashMap<>();
    private long currentId = 1;

    public void add(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot add null item");
        }
        item.setId(currentId++);
        storage.put(item.getId(), item);
    }

    public Item getById(long id) {
        Item item = storage.get(id);
        if (item == null) {
            throw new ItemNotFoundException("Item with ID " + id + " not found");
        }
        return item;
    }

    public void remove(long id) {
        getById(id); // Check if exists
        storage.remove(id);
    }

    public List<Item> getAll() {
        return new ArrayList<>(storage.values());
    }

    public void clear() {
        storage.clear();
        currentId = 1;
    }

    public Map<Long, Item> getStorage() {
        return storage;
    }

    public long getCurrentId() {
        return currentId;
    }

    public void setCurrentId(long currentId) {
        this.currentId = currentId;
    }

    public int size() {
        return storage.size();
    }

    public boolean isEmpty() {
        return storage.isEmpty();
    }
}