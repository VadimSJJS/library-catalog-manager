import console.ConsoleUI;
import repository.InMemoryCatalog;
import service.LibraryCatalogService;
import service.FileStorage;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        InMemoryCatalog catalog = new InMemoryCatalog();
        LibraryCatalogService service = new LibraryCatalogService(catalog);
        FileStorage fileStorage = new FileStorage();
        Scanner scanner = new Scanner(System.in);

        ConsoleUI ui = new ConsoleUI(service, fileStorage, scanner);
        ui.start();
    }
}