import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ManualInputDataSourceTest {

    @Test  // проверка правильно ли разбивает инпут на поля
    void testLoadStudents() {
        String input = "IT-21-3,4.5,123456\n" +
                "PI-22-1,3.8,654321\n" +
                "\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        ManualInputDataSource ds = new ManualInputDataSource(scanner);
        List<Student> students = ds.load();
        assertEquals(2, students.size());

        Student s1 = students.get(0);
        assertEquals("IT-21-3", s1.getGroup());
        assertEquals(4.5, s1.getAverage());
        assertEquals("123456", s1.getRecordBook());

        Student s2 = students.get(1);
        assertEquals("PI-22-1", s2.getGroup());
        assertEquals(3.8, s2.getAverage());
        assertEquals("654321", s2.getRecordBook());
    }
    @Test // тест на пропуск некорректного ввода студента
    void testSkipInvalidFormat() {
        String input = "IT-21-3,4.5,123456\n" +
                "some line\n" +
                "PI-22-1,3.8\n" +
                "\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        ManualInputDataSource ds = new ManualInputDataSource(scanner);
        List<Student> students = ds.load();
        assertEquals(1, students.size());
        assertEquals("IT-21-3", students.get(0).getGroup());
    }
    @Test //тест неверного average
    void testSkipInvalidAverageRange() {
        String input = "IT-21-3,6.0,123456\n" +
                "PI-22-1,-1.0,654321\n" +
                "TI-23-1,4.0,111111\n" +
                "\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        ManualInputDataSource ds = new ManualInputDataSource(scanner);
        List<Student> students = ds.load();
        assertEquals(1, students.size());
        assertEquals(4.0, students.get(0).getAverage());
    }
    // тест пустого листа
    @Test
    void testEmptyInput() {
        String input = "\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        ManualInputDataSource ds = new ManualInputDataSource(scanner);
        List<Student> students = ds.load();
        assertTrue(students.isEmpty());
    }
    //тест валидатора
    @Test
    void testValidator() {
        // Студент с пустой группой
        String input = ",4.5,123456\n" +
                "IT-21-3,4.5,123456\n" +
                "\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        ManualInputDataSource ds = new ManualInputDataSource(scanner);
        List<Student> students = ds.load();
        assertEquals(1, students.size());
        assertEquals("IT-21-3", students.get(0).getGroup());
    }
}
