import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class ComparatorFactoryTest {

    private Student s(String group, double avg, String rb) {
        return new Student.Builder().group(group).average(avg).recordBook(rb).build();
    }

    @Test
    void comparatorByGroupSort() {
        Comparator<Student> groupComp = ComparatorFactory.getComparator(1);
        assertTrue(groupComp.compare(s("А", 3.0, "1"), s("Б", 3.0, "1")) < 0);
        assertTrue(groupComp.compare(s("Б", 3.0, "1"), s("А", 3.0, "1")) > 0);
        assertEquals(0, groupComp.compare(s("А", 3.0, "1"), s("А", 5.0, "9")));
    }

    @Test
    void comparatorByAverageSort() {
        Comparator<Student> avgComp = ComparatorFactory.getComparator(2);
        assertTrue(avgComp.compare(s("А", 2.0, "1"), s("А", 4.0, "1")) < 0);
        assertEquals(0, avgComp.compare(s("А", 3.0, "1"), s("Б", 3.0, "2")));
    }

    @Test
    void comparatorByRecordBookSort() {
        Comparator<Student> recordComp = ComparatorFactory.getComparator(3);
        assertTrue(recordComp.compare(s("А", 3.0, "100"), s("А", 3.0, "200")) < 0);
    }

    @Test
    void comparatorByAllFieldsUsesGroup() {
        Comparator<Student> allComp = ComparatorFactory.getComparator(4);
        assertTrue(allComp.compare(s("А-1", 3.0, "001"), s("Б-1", 3.0, "001")) < 0);
    }

    @Test
    void comparatorByAllFieldsUsesThenAverage() {
        Comparator<Student> allComp = ComparatorFactory.getComparator(4);
        assertTrue(allComp.compare(s("А", 2.0, "001"), s("А", 4.0, "001")) < 0);
    }

    @Test
    void invalidChoiceThrows() {
        assertThrows(IllegalArgumentException.class, () -> ComparatorFactory.getComparator(0));
        assertThrows(IllegalArgumentException.class, () -> ComparatorFactory.getComparator(99));
    }
}
