package console;

import exception.InvalidDataException;
import exception.ItemNotFoundException;
import model.Book;
import model.Item;
import model.Magazine;
import service.LibraryCatalogService;
import service.FileStorage;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static util.ColorUtils.*;

public class ConsoleUI {
    private final LibraryCatalogService service;
    private final FileStorage fileStorage;
    private final Scanner scanner;
    private boolean isRunning;

    public ConsoleUI(LibraryCatalogService service, FileStorage fileStorage, Scanner scanner) {
        this.service = service;
        this.fileStorage = fileStorage;
        this.scanner = scanner;
        this.isRunning = true;
    }

    public void start() {
        while (isRunning) {
            showMenu();
            String userInput = scanner.nextLine();
            handleChoice(userInput);
        }
    }

    private void showMenu() {
        System.out.println(BOLD_CYAN + "=== LIBRARY CATALOG MENU ===" + RESET);
        System.out.println(GREEN + "1." + RESET + " Add a Book");
        System.out.println(GREEN + "2." + RESET + " Add a Magazine");
        System.out.println(RED + "3." + RESET + " Remove by ID");
        System.out.println(BLUE + "4." + RESET + " Show all items");
        System.out.println(BLUE + "5." + RESET + " Search by Title");
        System.out.println(BLUE + "6." + RESET + " Search by Author");
        System.out.println(BLUE + "7." + RESET + " Search by Year (published after)");
        System.out.println(YELLOW + "8." + RESET + " View Statistics");
        System.out.println(YELLOW + "9." + RESET + " Save to file");
        System.out.println(YELLOW + "10." + RESET + " Load from file");
        System.out.println(BOLD_RED + "0." + RESET + " Exit");
        System.out.print(BOLD_YELLOW + "Enter your choice: " + RESET);
    }

    private void handleChoice(String choice) {
        switch (choice) {
            case "1" -> addBook();
            case "2" -> addMagazine();
            case "3" -> removeById();
            case "4" -> showAllItems();
            case "5" -> searchByTitle();
            case "6" -> searchByAuthor();
            case "7" -> searchByYearAfter();
            case "8" -> showStatistics();
            case "9" -> saveToFile();
            case "10" -> loadFromFile();
            case "0" -> exit();
            default -> System.out.println("❌ Invalid choice. Please enter a number from 0 to 10.");
        }
    }

    private void addBook() {
        try {
            System.out.print(CYAN + "Enter the name of the book: " + RESET);
            String title = scanner.nextLine();

            System.out.print(CYAN + "Enter the year of the book's release: " + RESET);
            int year = Integer.parseInt(scanner.nextLine());

            System.out.print(CYAN + "Enter the name of the book's publisher: " + RESET);
            String publisher = scanner.nextLine();

            System.out.print(CYAN + "Enter the author of the book: " + RESET);
            String author = scanner.nextLine();

            System.out.print(CYAN + "Enter the number of pages of the book: " + RESET);
            int pageCount = Integer.parseInt(scanner.nextLine());

            System.out.print(CYAN + "Enter the isbn of the book: " + RESET);
            String isbn = scanner.nextLine();

            Item book = new Book(title, year, publisher, author, pageCount, isbn);
            service.addItem(book);
            System.out.println(BOLD_GREEN + "✅ The book was successfully added!" + RESET);

        } catch (NumberFormatException e) {
            System.out.println(BOLD_RED + "❌ Invalid input. Please enter a valid number." + RESET);
        } catch (InvalidDataException e) {
            System.out.println(BOLD_RED + "❌ " + e.getMessage() + RESET);
        }
    }

    private void addMagazine() {
        try {
            System.out.print(CYAN + "Enter the name of the magazine: " + RESET);
            String title = scanner.nextLine();

            System.out.print(CYAN + "Enter the year of the magazine's release: " + RESET);
            int year = Integer.parseInt(scanner.nextLine());

            System.out.print(CYAN + "Enter the name of the magazine's publisher: " + RESET);
            String publisher = scanner.nextLine();

            System.out.print(CYAN + "Enter the issue number of the magazine: " + RESET);
            int issueNumber = Integer.parseInt(scanner.nextLine());

            System.out.print(CYAN + "Enter the frequency of the magazine: " + RESET);
            String frequency = scanner.nextLine();

            Item magazine = new Magazine(title, year, publisher, issueNumber, frequency);
            service.addItem(magazine);
            System.out.println(BOLD_GREEN + "✅ The magazine was successfully added!" + RESET);

        } catch (NumberFormatException e) {
            System.out.println(BOLD_RED + "❌ Invalid input. Please enter a valid number." + RESET);
        } catch (InvalidDataException e) {
            System.out.println(BOLD_RED + "❌ " + e.getMessage() + RESET);
        }
    }

    private void removeById() {
        try {
            System.out.print("Enter ID: ");
            long id = Long.parseLong(scanner.nextLine());
            service.removeItem(id);
            System.out.println(BOLD_GREEN + "✅ Item with ID=" + id + " successfully deleted" + RESET);
        } catch (NumberFormatException e) {
            System.out.println(BOLD_RED + "❌ Invalid input. Please enter a valid number." + RESET);
        } catch (ItemNotFoundException e) {
            System.out.println(BOLD_RED + "❌ " + e.getMessage() + RESET);
        }
    }

    private void showAllItems() {
        List<Item> items = service.getAllItems();

        if (items.isEmpty()) {
            System.out.println(BOLD_YELLOW + "📭 The catalog is empty." + RESET);
            return;
        }

        System.out.println(BOLD_BLUE + "\n📚 All your items (" + items.size() + " total):" + RESET);
        for (int i = 0; i < items.size(); i++) {
            System.out.println(BOLD_CYAN + (i + 1) + "." + RESET + " " + items.get(i));
        }
        System.out.println();
    }

    private void searchByTitle() {
        System.out.print("Enter title: ");
        String keyword = scanner.nextLine();

        try {
            List<Item> results = service.searchByTitleKeyword(keyword);

            if (results.isEmpty()) {
                System.out.println(BOLD_YELLOW + "📭 No items found with title containing: " + keyword + RESET);
                return;
            }

            System.out.println(BOLD_BLUE + "\n🔍 Found " + results.size() + " item(s):" + RESET);
            for (int i = 0; i < results.size(); i++) {
                System.out.println(BOLD_CYAN + (i + 1) + "." + RESET + " " + results.get(i));
            }
            System.out.println();

        } catch (InvalidDataException e) {
            System.out.println(BOLD_RED + "❌ " + e.getMessage() + RESET);
        }
    }

    private void searchByAuthor() {
        System.out.print("Enter author: ");
        String author = scanner.nextLine();

        try {
            List<Book> results = service.searchBooksByAuthor(author);

            if (results.isEmpty()) {
                System.out.println(BOLD_YELLOW + "📭 No books found by author: " + author + RESET);
                return;
            }

            System.out.println(BOLD_BLUE + "\n🔍 Found " + results.size() + " book(s):" + RESET);
            for (int i = 0; i < results.size(); i++) {
                System.out.println(BOLD_CYAN + (i + 1) + "." + RESET + " " + results.get(i));
            }
            System.out.println();

        } catch (InvalidDataException e) {
            System.out.println(BOLD_RED + "❌ " + e.getMessage() + RESET);
        }
    }

    private void searchByYearAfter() {
        try {
            System.out.print("Enter year: ");
            int year = Integer.parseInt(scanner.nextLine());

            List<Item> results = service.findItemsPublishedAfter(year);

            if (results.isEmpty()) {
                System.out.println(BOLD_YELLOW + "📭 No items found published after " + year + RESET);
                return;
            }

            System.out.println(BOLD_BLUE + "\n🔍 Found " + results.size() + " item(s) published after " + year + ":" + RESET);
            for (int i = 0; i < results.size(); i++) {
                System.out.println(BOLD_CYAN + (i + 1) + "." + RESET + " " + results.get(i));
            }
            System.out.println();

        } catch (NumberFormatException e) {
            System.out.println(BOLD_RED + "❌ Invalid input. Please enter a valid number." + RESET);
        } catch (InvalidDataException e) {
            System.out.println(BOLD_RED + "❌ " + e.getMessage() + RESET);
        }
    }

    private void showStatistics() {
        Map<String, Long> stats = service.getTypeStatistics();

        if (stats.isEmpty()) {
            System.out.println(BOLD_YELLOW + "📭 The catalog is empty." + RESET);
            return;
        }

        System.out.println(BOLD_BLUE + "\n📊 Statistics:" + RESET);
        for (Map.Entry<String, Long> entry : stats.entrySet()) {
            System.out.println("   " + BOLD_CYAN + entry.getKey() + ":" + RESET + " " + entry.getValue());
        }

        long total = stats.values().stream().mapToLong(Long::longValue).sum();
        System.out.println("   " + BOLD_YELLOW + "Total:" + RESET + " " + total);
        System.out.println();
    }

    private void saveToFile() {
        try {
            System.out.print("Enter file name: ");
            String fileName = scanner.nextLine();
            fileStorage.saveToFile(service.getStorage(), fileName);
            System.out.println(BOLD_GREEN + "✅ Catalog saved to: " + fileName + RESET);
        } catch (RuntimeException e) {
            System.out.println(BOLD_RED + "❌ " + e.getMessage() + RESET);
        }
    }

    private void loadFromFile() {
        try {
            System.out.print("Enter file name: ");
            String fileName = scanner.nextLine();
            Map<Long, Item> loaded = fileStorage.loadFromFile(fileName);
            service.restoreFromStorage(loaded);
            System.out.println(BOLD_GREEN + "✅ Catalog loaded from: " + fileName + RESET);
        } catch (RuntimeException e) {
            System.out.println(BOLD_RED + "❌ " + e.getMessage() + RESET);
        }
    }

    private void exit() {
        System.out.println(BOLD_GREEN + "👋 Goodbye!" + RESET);
        isRunning = false;
    }
}
