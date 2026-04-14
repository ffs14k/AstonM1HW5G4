import org.junit.jupiter.api.Test;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

public class RandomDataSourceTest {
    @Test // тест на генерацию только валидных
    void testAllStudentsAreValid() {
        RandomDataSource ds = new RandomDataSource(50);
        List<Student> students = ds.load();
        StudentValidator validator = new StudentValidator();
        for (Student s : students) {
            assertDoesNotThrow(() -> validator.validate(s));  // StudentValidator не кидает ошибки
        }
    }
    @Test // тест на генерацию нужного количества
    void testCountMatchesRequest() {
        RandomDataSource ds = new RandomDataSource(20);
        List<Student> students = ds.load();
        assertEquals(20, students.size());
    }
    @Test // тест на пустоту списка если ничего не сгенерировано
    void testEmptyWhenCountZero() {
        RandomDataSource ds = new RandomDataSource(0);
        List<Student> students = ds.load();
        assertTrue(students.isEmpty());
    }
}
