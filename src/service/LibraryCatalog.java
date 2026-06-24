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
    private final Map<Long, Item> storage = new HashMap<>(); // {id:item}
    private long currentId = 1;

    public void addItem(Item item) {
        if (item == null) {
            throw new InvalidDataException("Нельзя добавить null в каталог");
        }
        if (storage.containsKey(item.getId())) {
            throw new InvalidDataException(("Элемент с ID " + item.getId() + " уже существует"));
        }
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
            System.out.println("Каталог успешно сохранен в файл: " + fileName);
        } catch (IOException ex) {
            System.err.println("Ошибка при сохранении файла: " + ex.getMessage());
            throw new RuntimeException("Не удалось сохранить файл", ex);
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
                        System.err.println("Пропущена битая строка: " + line);
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
                        System.err.println("Неизвестный тип объекта: " + splitLine[0]);
                        continue;
                    }

                    item.setId(id);
                    storage.put(id, item);

                } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                    System.err.println("Ошибка парсинга строки (пропущена): " + line);
                    System.err.println("Причина: " + ex.getMessage());
                }
            }

            long maxId = storage.keySet().stream().max(Long::compareTo).orElse(0L);
            currentId = maxId + 1;

            System.out.println("Загружено " + storage.size() + " элементов из файла: " + fileName);

        } catch (FileNotFoundException ex) {
            System.out.println("Файл не найден. Будет создан новый каталог.");
        } catch (IOException ex) {
            System.err.println("Ошибка при чтении файла: " + ex.getMessage());
            throw new RuntimeException("Не удалось загрузить файл", ex);
        }
    }

    /*
    STREAM API
    */

    public List<Item> searchByTitleKeyword(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            throw new InvalidDataException("Заголовок не может быть пустым или null");
        }

        return storage.values().stream()
            .filter(item -> item.getTitle().toLowerCase().contains(keyword.toLowerCase()))
            .toList();
    }

    public List<Book> searchBooksByAuthor(String author) {
        if (author == null || author.isEmpty()) {
            throw new InvalidDataException("Имя автора не может быть пустым или null");
        }

        return storage.values().stream()
            .filter(item -> item instanceof Book)
            .map(item -> (Book)item)
            .filter(book -> book.getAuthor().equalsIgnoreCase(author))
            .toList();
    }

    public List<Item> findItemsPublishedAfter(int year) throws InvalidDataException {
        if (year < 0) {
            throw new InvalidDataException("Год не может быть отрицательным");
        }

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

    public void clear() {
        storage.clear();
        currentId = 1;
    }
}
