import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {

    private final StudentValidator validator = new StudentValidator();

    private Student s(String group, double avg, String rb) {
        return new Student.Builder().group(group).average(avg).recordBook(rb).build();
    }

    @Test
    void validStudentPassesValidation() {
        assertDoesNotThrow(() -> validator.validate(s("ИТ-21-1", 4.0, "100001")));
    }

    @Test
    void zeroAverageIsValid() {
        assertDoesNotThrow(() -> validator.validate(s("А", 0.0, "1")));
    }

    @Test
    void fiveAverageIsValid() {
        assertDoesNotThrow(() -> validator.validate(s("А", 5.0, "1")));
    }

    @Test
    void averageAboveFiveFails() {
        assertThrows(IllegalArgumentException.class, () -> validator.validate(s("А", 5.1, "1")));
    }

    @Test
    void averageBelowZeroFails() {
        assertThrows(IllegalArgumentException.class, () -> validator.validate(s("А", -0.1, "1")));
    }

    @Test
    void emptyGroupFails() {
        assertThrows(IllegalArgumentException.class, () -> validator.validate(s("", 4.0, "1")));
    }

    @Test
    void blankGroupFails() {
        assertThrows(IllegalArgumentException.class, () -> validator.validate(s("   ", 4.0, "1")));
    }

    @Test
    void nullGroupFails() {
        assertThrows(IllegalArgumentException.class, () -> validator.validate(s(null, 4.0, "1")));
    }

    @Test
    void emptyRecordBookFails() {
        assertThrows(IllegalArgumentException.class, () -> validator.validate(s("А", 4.0, "")));
    }

    @Test
    void nullStudentFails() {
        assertThrows(IllegalArgumentException.class, () -> validator.validate(null));
    }
}
