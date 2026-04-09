import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StudentCsvFormatterTest {

    private final StudentCsvFormatter formatter = new StudentCsvFormatter(new StudentValidator());

    @Test
    // Проверяет корректное преобразование Student в CSV-строку.
    void formatsStudentToCsv() {
        Student student = new Student.Builder()
                .group("ИТ-21-1")
                .average(4.5)
                .recordBook("100001")
                .build();

        assertEquals("ИТ-21-1,4.5,100001", formatter.format(student));
    }

    @Test
    // Проверяет корректный парсинг CSV-строки в Student.
    void parsesCsvLineToStudent() {
        Student student = formatter.parse("ИТ-21-1,4.5,100001");

        assertEquals("ИТ-21-1", student.getGroup());
        assertEquals(4.5, student.getAverage(), 0.001);
        assertEquals("100001", student.getRecordBook());
    }

    @Test
    // Проверяет ошибку на некорректный CSV-формат (недостаточно полей).
    void throwsOnInvalidFieldCount() {
        assertThrows(IllegalArgumentException.class, () -> formatter.parse("ИТ-21-1,4.5"));
    }
}
