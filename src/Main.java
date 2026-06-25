import exception.InvalidDataException;
import exception.ItemNotFoundException;
import model.Book;
import model.Item;
import model.Magazine;
import service.LibraryCatalog;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static util.ColorUtils.*;

public class Main {
    static LibraryCatalog libraryCatalog = new LibraryCatalog();
    static Scanner scanner = new Scanner(System.in);
    static boolean isRunning = true;

    public static void main(String[] args) {
        while (isRunning) {
            showMenu();
            String userInput = scanner.nextLine();

            switch (userInput) {
                case "1":
                    addBook();
                    break;
                case "2":
                    addMagazine();
                    break;
                case "3":
                    removeById();
                    break;
                case "4":
                    showAllItems();
                    break;
                case "5":
                    searchByTitle();
                    break;
                case "6":
                    searchByAuthor();
                    break;
                case "7":
                    searchByYearAfter();
                    break;
                case "8":
                    showStatistics();
                    break;
                case "9":
                    saveToFile();
                    break;
                case "10":
                    loadFromFile();
                    break;
                case "0":
                    exit();
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number from 0 to 10.");
                    break;
            }
        }
    }

    public static void showMenu() {
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

    // 1. Add a Book
    public static void addBook() {
        try {
            System.out.print(CYAN + "Enter the name of the book: " + RESET);
            String bookTitleInput = scanner.nextLine();

            System.out.print(CYAN + "Enter the year of the book's release: " + RESET);
            int bookYearPublishedInput = Integer.parseInt(scanner.nextLine());

            System.out.print(CYAN + "Enter the name of the book's publisher: " + RESET);
            String bookPublisherInput = scanner.nextLine();

            System.out.print(CYAN + "Enter the author of the book: " + RESET);
            String bookAuthorInput = scanner.nextLine();

            System.out.print(CYAN + "Enter the number of pages of the book: " + RESET);
            int bookPageCountInput = Integer.parseInt(scanner.nextLine());

            System.out.print(CYAN + "Enter the isbn of the book: " + RESET);
            String bookISBNInput = scanner.nextLine();

            Item book = new Book(bookTitleInput, bookYearPublishedInput, bookPublisherInput,
                    bookAuthorInput, bookPageCountInput, bookISBNInput);
            libraryCatalog.addItem(book);
            System.out.println(BOLD_GREEN + "✅ The book was successfully added!" + RESET);

        } catch (NumberFormatException e) {
            System.out.println(BOLD_RED + "❌ Invalid input. Please enter a valid number." + RESET);
        } catch (InvalidDataException e) {
            System.out.println(BOLD_RED + "❌ Invalid data: " + e.getMessage() + RESET);
        }
    }

    // 2. Add a Magazine
    public static void addMagazine() {
        try {
            System.out.print(CYAN + "Enter the name of the magazine: " + RESET);
            String magazineTitleInput = scanner.nextLine();

            System.out.print(CYAN + "Enter the year of the magazine's release: " + RESET);
            int magazineYearPublishedInput = Integer.parseInt(scanner.nextLine());

            System.out.print(CYAN + "Enter the name of the magazine's publisher: " + RESET);
            String magazinePublisherInput = scanner.nextLine();

            System.out.print(CYAN + "Enter the issue number of the magazine: " + RESET);
            int magazineIssueNumberInput = Integer.parseInt(scanner.nextLine());

            System.out.print(CYAN + "Enter the frequency of the magazine: " + RESET);
            String magazineFrequencyInput = scanner.nextLine();

            Item magazine = new Magazine(magazineTitleInput, magazineYearPublishedInput, magazinePublisherInput, magazineIssueNumberInput, magazineFrequencyInput);
            libraryCatalog.addItem(magazine);
            System.out.println(BOLD_GREEN + "✅ The magazine was successfully added!" + RESET);

        } catch (NumberFormatException e) {
            System.out.println(BOLD_RED + "❌ Invalid input. Please enter a valid number." + RESET);
        } catch (InvalidDataException e) {
            System.out.println(BOLD_RED + "❌ Invalid data: " + e.getMessage() + RESET);
        }
    }

    // 3. Remove by ID
    public static void removeById() {
        try {
            System.out.print("Input ID: ");
            long itemIdInput = Long.parseLong(scanner.nextLine());

            libraryCatalog.removeItem(itemIdInput);
            System.out.println(BOLD_GREEN + "✅ Item with ID=" + itemIdInput + " successfully deleted" + RESET);

        } catch (NumberFormatException e) {
            System.out.println(BOLD_RED + "❌ Invalid input. Please enter a valid number." + RESET);
        } catch (ItemNotFoundException e) {
            System.out.println(BOLD_RED + "❌ " + e.getMessage() + RESET);
        }
    }

    // 4. Sow all items
    public static void showAllItems() {
        List<Item> items = libraryCatalog.getAllItems();

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

    // 5. Search by Title
    public static void searchByTitle() {
        System.out.print("Input title: ");
        String titleInput = scanner.nextLine();

        List<Item> results = libraryCatalog.searchByTitleKeyword(titleInput);

        if (results.isEmpty()) {
            System.out.println(BOLD_YELLOW + "📭 No items found with title containing: " + titleInput + RESET);
            return;
        }

        System.out.println(BOLD_BLUE + "\n🔍 Found " + results.size() + " item(s) with title containing '" + titleInput + "':" + RESET);
        for (int i = 0; i < results.size(); i++) {
            System.out.println(BOLD_CYAN + (i + 1) + "." + RESET + " " + results.get(i));
        }
        System.out.println();
    }

    // 6. Search by Author
    public static void searchByAuthor() {
        System.out.print("Input author: ");
        String authorInput = scanner.nextLine();

        List<Book> results = libraryCatalog.searchBooksByAuthor(authorInput);

        if (results.isEmpty()) {
            System.out.println(BOLD_YELLOW + "📭 No books found by author: " + authorInput + RESET);
            return;
        }

        System.out.println(BOLD_BLUE + "\n🔍 Found " + results.size() + " book(s) by author '" + authorInput + "':" + RESET);
        for (int i = 0; i < results.size(); i++) {
            System.out.println(BOLD_CYAN + (i + 1) + "." + RESET + " " + results.get(i));
        }
        System.out.println();
    }

    // 7. Search by Year (published after)
    public static void searchByYearAfter() {
        try {
            System.out.print("Input year: ");
            int yearInput = Integer.parseInt(scanner.nextLine());

            List<Item> results = libraryCatalog.findItemsPublishedAfter(yearInput);

            if (results.isEmpty()) {
                System.out.println(BOLD_YELLOW + "📭 No items found published after " + yearInput + RESET);
                return;
            }

            System.out.println(BOLD_BLUE + "\n🔍 Found " + results.size() + " item(s) published after " + yearInput + ":" + RESET);
            for (int i = 0; i < results.size(); i++) {
                System.out.println(BOLD_CYAN + (i + 1) + "." + RESET + " " + results.get(i));
            }
            System.out.println();

        } catch (NumberFormatException e) {
            System.out.println(BOLD_RED + "❌ Invalid input. Please enter a valid number." + RESET);
        }
    }

    // 8. View Statistics
    public static void showStatistics() {
        Map<String, Long> statistics = libraryCatalog.getTypeStatistics();

        if (statistics.isEmpty()) {
            System.out.println(BOLD_YELLOW + "📭 The catalog is empty." + RESET);
            return;
        }

        System.out.println(BOLD_BLUE + "\n📊 Statistics:" + RESET);
        for (Map.Entry<String, Long> entry : statistics.entrySet()) {
            System.out.println("   " + BOLD_CYAN + entry.getKey() + ":" + RESET + " " + entry.getValue());
        }

        long total = statistics.values().stream().mapToLong(Long::longValue).sum();
        System.out.println("   " + BOLD_YELLOW + "Total:" + RESET + " " + total);
        System.out.println();
    }

    // 9. Save to file
    public static void saveToFile() {
        try {
            System.out.print("Input the file name: ");
            String fileNameInput = scanner.nextLine();
            libraryCatalog.saveToFile(fileNameInput);
            System.out.println(BOLD_GREEN + "✅ Catalog saved to file: " + fileNameInput + RESET);
        } catch (RuntimeException e) {
            System.out.println(BOLD_RED + "❌ " + e.getMessage() + RESET);
        }
    }

    // 10. Load from file
    public static void loadFromFile() {
        try {
            System.out.print("Input the file name: ");
            String fileNameInput = scanner.nextLine();
            libraryCatalog.loadFromFile(fileNameInput);
            System.out.println(BOLD_GREEN + "✅ Catalog loaded from file: " + fileNameInput + RESET);
        } catch (RuntimeException e) {
            System.out.println(BOLD_RED + "❌ " + e.getMessage() + RESET);
        }
    }

    // 0. Exit
    public static void exit() {
        System.out.println(BOLD_GREEN + "👋 Goodbye!" + RESET);
        isRunning = false;
        scanner.close();
    }
}
