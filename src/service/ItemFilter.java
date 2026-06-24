package service;

import model.Item;

// Функциональный интерфейс (стратегия)
public interface ItemFilter {
    boolean test(Item item);
} 