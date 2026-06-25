package model;

import exception.InvalidDataException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MagazineTest {
    @Test
    void shouldThrowExceptionWhenYearPublishedIsNegative() {
        assertThrows(InvalidDataException.class, () -> {
            new Magazine("1", -1, "a", 2, "2");
        });
    }

    @Test
    void shouldThrowExceptionWhenYearPublishedIsMoreThanCurrent() {
        assertThrows(InvalidDataException.class, () -> {
            new Magazine("1", 2027, "a", 2, "2");
        });
    }

    @Test
    void shouldThrowExceptionWhenIssueNumberIsNegative() {
        assertThrows(InvalidDataException.class, () -> {
            new Magazine("1", -1, "a", -2, "2");
        });
    }
}
