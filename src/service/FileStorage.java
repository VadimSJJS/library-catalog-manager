package service;

import model.Book;
import model.Item;
import model.Magazine;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileStorage {

    public void saveToFile(Map<Long, Item> storage, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Item item : storage.values()) {
                String line = convertItemToString(item);
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save catalog to file: " + fileName, e);
        }
    }

    public Map<Long, Item> loadFromFile(String fileName) {
        Map<Long, Item> storage = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    Item item = parseLineToItem(line);
                    storage.put(item.getId(), item);
                } catch (Exception e) {
                    throw new RuntimeException("Parse exception line!");
                }
            }
        } catch (FileNotFoundException e) {
            // return empty storage
            return storage;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load catalog from file: " + fileName, e);
        }

        return storage;
    }

    private String convertItemToString(Item item) {
        if (item instanceof Book book) {
            return "BOOK|" + book.getId() + "|" + book.getTitle() + "|" +
                    book.getYearPublished() + "|" + book.getPublisher() + "|" +
                    book.getAuthor() + "|" + book.getPageCount() + "|" + book.getIsbn();
        } else if (item instanceof Magazine magazine) {
            return "MAGAZINE|" + magazine.getId() + "|" + magazine.getTitle() + "|" +
                    magazine.getYearPublished() + "|" + magazine.getPublisher() + "|" +
                    magazine.getIssueNumber() + "|" + magazine.getFrequency();
        }
        throw new IllegalArgumentException("Unknown item type: " + item.getClass());
    }

    private Item parseLineToItem(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 7) {
            throw new IllegalArgumentException("Invalid format: " + line);
        }

        long id = Long.parseLong(parts[1]);

        if (parts[0].equals("BOOK")) {
            Book book = new Book(
                    parts[2],
                    Integer.parseInt(parts[3]),
                    parts[4],
                    parts[5],
                    Integer.parseInt(parts[6]),
                    parts[7]
            );
            book.setId(id);
            return book;
        } else if (parts[0].equals("MAGAZINE")) {
            Magazine magazine = new Magazine(
                    parts[2],
                    Integer.parseInt(parts[3]),
                    parts[4],
                    Integer.parseInt(parts[5]),
                    parts[6]
            );
            magazine.setId(id);
            return magazine;
        }

        throw new IllegalArgumentException("Unknown type: " + parts[0]);
    }
}