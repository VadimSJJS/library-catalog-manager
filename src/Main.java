import model.Book;
import model.Item;
import model.Magazine;

public class Main {
    public static void main(String[] args) {
        Item book = new Book("Book 1", 2003, "Publisher 1", "Author 1", 2, "isbn 1");
        Item magazine = new Magazine("Magazine 1", 2003, "Publisher 1", 1, "frequency 1");

        System.out.println(book.getDescription());
        System.out.println(magazine.getDescription());
    }
}
