import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileDataSourceTest {

    @TempDir
    Path tempDir;

    private Path csv(String content) throws IOException {
        Path file = tempDir.resolve("students.csv");
        Files.writeString(file, content);
        return file;
    }

    @Test
    void loadsStudentsFromValidFile() throws IOException {
        Path file = csv("group,average,recordBook\nИТ-21-1,4.5,100001\nИТ-21-2,3.8,100002\n");
        List<Student> students = new FileDataSource(file.toString()).load();

        assertEquals(2, students.size());
        assertEquals("ИТ-21-1", students.getFirst().getGroup());
        assertEquals(4.5, students.getFirst().getAverage(), 0.001);
        assertEquals("100001", students.getFirst().getRecordBook());
    }

    @Test
    void skipsBlankLines() throws IOException {
        Path file = csv("group,average,recordBook\nИТ-21-1,4.0,100001\n\nИТ-21-2,3.0,100002\n");
        assertEquals(2, new FileDataSource(file.toString()).load().size());
    }

    @Test
    void throwsOnInvalidAverageFormat() throws IOException {
        Path file = csv("group,average,recordBook\nИТ-21-1,не_число,100001\n");
        assertThrows(RuntimeException.class, () -> new FileDataSource(file.toString()).load());
    }

    @Test
    void throwsWhenAverageExceedsFive() throws IOException {
        Path file = csv("group,average,recordBook\nИТ-21-1,6.0,100001\n");
        assertThrows(RuntimeException.class, () -> new FileDataSource(file.toString()).load());
    }

    @Test
    void throwsWhenAverageIsNegative() throws IOException {
        Path file = csv("group,average,recordBook\nИТ-21-1,-1.0,100001\n");
        assertThrows(RuntimeException.class, () -> new FileDataSource(file.toString()).load());
    }

    @Test
    void throwsWhenRowHasTooFewColumns() throws IOException {
        Path file = csv("group,average,recordBook\nИТ-21-1,4.0\n");
        assertThrows(RuntimeException.class, () -> new FileDataSource(file.toString()).load());
    }

    @Test
    void throwsWhenFileDoesNotExist() {
        assertThrows(RuntimeException.class, () -> new FileDataSource("нет_такого.csv").load());
    }
}
