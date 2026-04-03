import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileWriterServiceTest {

    @TempDir
    Path tempDir;

    private Student s(String group, double avg, String rb) {
        return new Student.Builder().group(group).average(avg).recordBook(rb).build();
    }

    @Test
    // Проверяет базовую запись списка студентов в CSV.
    void writesStudentsAsCsvLinesInAppendMode() throws IOException {
        Path file = tempDir.resolve("students.csv");
        Files.writeString(file, "group,average,recordBook\n");

        FileWriterService writer = new FileWriterService(file.toString());
        writer.writeToFile(List.of(
                s("ИТ-21-1", 4.5, "100001"),
                s("ИТ-21-2", 3.8, "100002")
        ));

        assertEquals(
                List.of(
                        "group,average,recordBook",
                        "ИТ-21-1,4.5,100001",
                        "ИТ-21-2,3.8,100002"
                ),
                Files.readAllLines(file)
        );
    }

    @Test
    // Проверяет, что второй вызов writeToFile дописывает строки, а не затирает файл.
    void appendsOnSecondWrite() throws IOException {
        Path file = tempDir.resolve("students.csv");
        Files.writeString(file, "group,average,recordBook\n");
        FileWriterService writer = new FileWriterService(file.toString());

        writer.writeToFile(List.of(s("ИТ-21-1", 4.5, "100001")));
        writer.writeToFile(List.of(s("ИТ-21-2", 3.8, "100002")));

        assertEquals(
                List.of(
                        "group,average,recordBook",
                        "ИТ-21-1,4.5,100001",
                        "ИТ-21-2,3.8,100002"
                ),
                Files.readAllLines(file)
        );
    }

    @Test
    // Проверяет, что пустой список не меняет содержимое файла.
    void writesEmptyListWithoutChanges() throws IOException {
        Path file = tempDir.resolve("students.csv");
        Files.writeString(file, "group,average,recordBook\n");
        FileWriterService writer = new FileWriterService(file.toString());

        writer.writeToFile(List.of());

        assertEquals(
                List.of("group,average,recordBook"),
                Files.readAllLines(file)
        );
    }

    @Test
    // Проверяет точный CSV-формат одной записи.
    void formatCsvProducesExpectedLine() {
        FileWriterService writer = new FileWriterService("unused.csv");

        assertEquals(
                "ИТ-21-1,4.5,100001",
                writer.formatCsv(s("ИТ-21-1", 4.5, "100001"))
        );
    }
}
