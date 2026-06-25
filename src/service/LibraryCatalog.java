package service;

import exception.InvalidDataException;
import exception.ItemNotFoundException;
import model.Book;
import model.Item;
import model.Magazine;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LibraryCatalog {
    private final Map<Long, Item> storage = new HashMap<>();
    private long currentId = 1;

    public void addItem(Item item) {
        if (item == null) {
            throw new InvalidDataException("Cannot add null item to catalog");
        }
        if (storage.containsKey(item.getId())) {
            throw new InvalidDataException("Item with ID " + item.getId() + " already exists");
        }
        item.setId(currentId++);
        storage.put(item.getId(), item);
    }

    public Item getItemById(long id) {
        Item item = storage.get(id);

        if (item == null) {
            throw new ItemNotFoundException("Item with ID " + id + " not found");
        }

        return item;
    }

    public void removeItem(long id) {
        getItemById(id); // Check if item exists before removal
        storage.remove(id);
    }

    public List<Item> getAllItems() {
        return new ArrayList<>(storage.values());
    }

    public void saveToFile(String fileName) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {
            List<Item> items = getAllItems();
            for (Item item : items) {
                String line;
                if (item instanceof Book) {
                    Book book = (Book) item;
                    line = "BOOK|" + book.getId() + "|" + book.getTitle() + "|" +
                            book.getYearPublished() + "|" + book.getPublisher() + "|" +
                            book.getAuthor() + "|" + book.getPageCount() + "|" + book.getIsbn();
                } else {
                    Magazine magazine = (Magazine) item;
                    line = "MAGAZINE|" + magazine.getId() + "|" + magazine.getTitle() + "|" +
                            magazine.getYearPublished() + "|" + magazine.getPublisher() + "|" +
                            magazine.getIssueNumber() + "|" + magazine.getFrequency();
                }
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            System.out.println("Catalog successfully saved to file: " + fileName);
        } catch (IOException ex) {
            System.err.println("Error saving file: " + ex.getMessage());
            throw new RuntimeException("Failed to save file", ex);
        }
    }

    public void loadFromFile(String fileName) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            storage.clear();
            currentId = 1;

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                try {
                    String[] splitLine = line.split("\\|");
                    if (splitLine.length < 7) {
                        System.err.println("Skipping malformed line: " + line);
                        continue;
                    }

                    Item item;
                    long id = Long.parseLong(splitLine[1]);

                    if (splitLine[0].equals("BOOK")) {
                        String bookTitle = splitLine[2];
                        int bookYearPublished = Integer.parseInt(splitLine[3]);
                        String bookPublisher = splitLine[4];
                        String bookAuthor = splitLine[5];
                        int bookPageCount = Integer.parseInt(splitLine[6]);
                        String bookIsbn = splitLine[7];

                        item = new Book(bookTitle, bookYearPublished, bookPublisher,
                                bookAuthor, bookPageCount, bookIsbn);
                    } else if (splitLine[0].equals("MAGAZINE")) {
                        String magazineTitle = splitLine[2];
                        int magazineYearPublished = Integer.parseInt(splitLine[3]);
                        String magazinePublisher = splitLine[4];
                        int magazineIssueNumber = Integer.parseInt(splitLine[5]);
                        String magazineFrequency = splitLine[6];

                        item = new Magazine(magazineTitle, magazineYearPublished, magazinePublisher,
                                magazineIssueNumber, magazineFrequency);
                    } else {
                        System.err.println("Unknown object type: " + splitLine[0]);
                        continue;
                    }

                    item.setId(id);
                    storage.put(id, item);

                } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                    System.err.println("Error parsing line (skipped): " + line);
                    System.err.println("Reason: " + ex.getMessage());
                }
            }

            long maxId = storage.keySet().stream().max(Long::compareTo).orElse(0L);
            currentId = maxId + 1;

            System.out.println("Loaded " + storage.size() + " items from file: " + fileName);

        } catch (FileNotFoundException ex) {
            System.out.println("File not found. A new catalog will be created.");
        } catch (IOException ex) {
            System.err.println("Error reading file: " + ex.getMessage());
            throw new RuntimeException("Failed to load file", ex);
        }
    }

    /*
     * STREAM API METHODS
     */

    public List<Item> searchByTitleKeyword(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            throw new InvalidDataException("Keyword cannot be null or empty");
        }

        return storage.values().stream()
                .filter(item -> item.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
    }

    public List<Book> searchBooksByAuthor(String author) {
        if (author == null || author.isEmpty()) {
            throw new InvalidDataException("Author name cannot be null or empty");
        }

        return storage.values().stream()
                .filter(item -> item instanceof Book)
                .map(item -> (Book) item)
                .filter(book -> book.getAuthor().equalsIgnoreCase(author))
                .toList();
    }

    public List<Item> findItemsPublishedAfter(int year) throws InvalidDataException {
        if (year < 0) {
            throw new InvalidDataException("Year cannot be negative");
        }

        return storage.values().stream()
                .filter(item -> item.getYearPublished() >= year)
                .toList();
    }

    public Map<String, Long> getTypeStatistics() {
        return storage.values().stream()
                .collect(Collectors.groupingBy(
                        item -> item instanceof Book ? "Book" : "Magazine",
                        Collectors.counting()
                ));
    }

    public List<Item> filterItems(ItemFilter filter) {
        if (filter == null) {
            throw new InvalidDataException("Filter cannot be null");
        }

        return storage.values().stream()
                .filter(filter::test)
                .collect(Collectors.toList());
    }

    public void clear() {
        storage.clear();
        currentId = 1;
    }
}