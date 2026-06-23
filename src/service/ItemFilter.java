package service;

import model.Item;

// Functional interface
interface ItemFilter {
    boolean test(Item item);
}