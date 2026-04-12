import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OccurrencesCounterServiceTest {

    private final OccurrencesCounterService service = new OccurrencesCounterService();

    private Student s(String group, double avg, String rb) {
        return new Student.Builder().group(group).average(avg).recordBook(rb).build();
    }

    @Test
    void countsExactOccurrences() {
        Student target = s("ИТ-21-1", 4.0, "100001");
        List<Student> list = new ArrayList<>();
        list.add(s("ИТ-21-1", 4.0, "100001"));
        list.add(s("ИТ-21-2", 3.0, "100002"));
        list.add(s("ИТ-21-1", 4.0, "100001"));
        list.add(s("ИТ-21-3", 5.0, "100003"));

        assertEquals(2, service.countOccurrences(list, target));
    }

    @Test
    void returnsZeroWhenNoMatch() {
        Student target = s("ИТ-99-9", 1.0, "999999");
        List<Student> list = new ArrayList<>();
        list.add(s("ИТ-21-1", 4.0, "100001"));
        list.add(s("ИТ-21-2", 3.0, "100002"));

        assertEquals(0, service.countOccurrences(list, target));
    }

    @Test
    void returnsZeroForEmptyList() {
        assertEquals(0, service.countOccurrences(new ArrayList<>(), s("А", 3.0, "1")));
    }

    @Test
    void countsAllWhenAllMatch() {
        Student target = s("ИТ-21-1", 4.0, "100001");
        List<Student> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(s("ИТ-21-1", 4.0, "100001"));
        }

        assertEquals(10, service.countOccurrences(list, target));
    }

    @Test
    void worksWithLargeList() {
        Student target = s("Целевой", 3.0, "000");
        List<Student> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add(s("Студент-" + i, 3.0, String.valueOf(i)));
        }
        for (int i = 0; i < 7; i++) {
            list.add(target);
        }

        assertEquals(7, service.countOccurrences(list, target));
    }
}
