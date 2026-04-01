import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BubbleSortTest {

    private final BubbleSort<Student> sort = new BubbleSort<>();

    private Student s(String group, double avg, String rb) {
        return new Student.Builder().group(group).average(avg).recordBook(rb).build();
    }

    @Test
    void sortsByAverageAscending() {
        List<Student> list = new ArrayList<>(Arrays.asList(
                s("А", 4.0, "1"),
                s("Б", 1.0, "2"),
                s("В", 3.0, "3"))
        );

        sort.sort(list, Comparator.comparingDouble(Student::getAverage));

        assertEquals(1.0, list.get(0).getAverage(), 0.001);
        assertEquals(3.0, list.get(1).getAverage(), 0.001);
        assertEquals(4.0, list.get(2).getAverage(), 0.001);
    }

    @Test
    void sortsByGroupAscending() {
        List<Student> list = new ArrayList<>(Arrays.asList(
                s("В", 3.0, "1"),
                s("А", 3.0, "2"),
                s("Б", 3.0, "3"))
        );

        sort.sort(list, Comparator.comparing(Student::getGroup));

        assertEquals("А", list.get(0).getGroup());
        assertEquals("Б", list.get(1).getGroup());
        assertEquals("В", list.get(2).getGroup());
    }

    @Test
    void sortsByRecordBookAscending() {
        List<Student> list = new ArrayList<>(Arrays.asList(
                s("А", 3.0, "300"),
                s("Б", 3.0, "100"),
                s("В", 3.0, "200"))
        );

        sort.sort(list, Comparator.comparing(Student::getRecordBook));

        assertEquals("100", list.get(0).getRecordBook());
        assertEquals("200", list.get(1).getRecordBook());
        assertEquals("300", list.get(2).getRecordBook());
    }

    @Test
    void emptyListDoesNotThrow() {
        assertDoesNotThrow(() -> sort.sort(new ArrayList<>(), Comparator.comparing(Student::getGroup)));
    }

    @Test
    void singleElementListRemainsSame() {
        Student st = s("А", 3.0, "1");
        List<Student> list = new ArrayList<>(List.of(st));
        sort.sort(list, Comparator.comparing(Student::getGroup));
        assertEquals(st, list.getFirst());
    }

    @Test
    void reverseSortedListGetsSorted() {
        List<Student> list = new ArrayList<>(Arrays.asList(
                s("В", 5.0, "3"),
                s("Б", 3.0, "2"),
                s("А", 1.0, "1"))
        );

        sort.sort(list, Comparator.comparingDouble(Student::getAverage));

        assertEquals(1.0, list.get(0).getAverage(), 0.001);
        assertEquals(5.0, list.get(2).getAverage(), 0.001);
    }
}
