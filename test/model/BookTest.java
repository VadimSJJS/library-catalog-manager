package model;

import exception.InvalidDataException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class BookTest {
    @Test
    void shouldThrowExceptionWhenPageCountIsNegative() {
        assertThrows(InvalidDataException.class, () -> {
            new Book("Invalid Book", 2020, "Publisher", "Author", -10, "12345");
        });
    }

    @Test
    void shouldThrowExceptionWhenYearPublishedIsNegative() {
        assertThrows(InvalidDataException.class, () -> {
            new Book("Invalid Book", -1, "Publisher", "Author", 20, "12345");
        });
    }

    @Test
    void shouldThrowExceptionWhenYearPublishedIsMoreThanCurrent() {
        assertThrows(InvalidDataException.class, () -> {
            new Book("Invalid Book", 2027, "Publisher", "Author", 20, "12345");
        });
    }
}
