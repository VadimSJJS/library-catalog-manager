package service;

import model.Item;

// Functional interface (strategy)
public interface ItemFilter {
    boolean test(Item item);
} 