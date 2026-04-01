import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentBuilderTest {

    private Student s(String group, double avg, String rb) {
        return new Student.Builder().group(group).average(avg).recordBook(rb).build();
    }

    @Test
    void builderCreatesStudentWithCorrectFields() {
        Student student = s("ИТ-21-1", 4.5, "100001");
        assertEquals("ИТ-21-1", student.getGroup());
        assertEquals(4.5, student.getAverage(), 0.001);
        assertEquals("100001", student.getRecordBook());
    }

    @Test
    void equalStudentsAreEqual() {
        assertEquals(s("А", 3.0, "1"), s("А", 3.0, "1"));
    }

    @Test
    void equalStudentsHaveSameHashCode() {
        assertEquals(s("А", 3.0, "1").hashCode(), s("А", 3.0, "1").hashCode());
    }

    @Test
    void differentGroupMakesNotEqual() {
        assertNotEquals(s("А", 3.0, "1"), s("Б", 3.0, "1"));
    }

    @Test
    void differentAverageMakesNotEqual() {
        assertNotEquals(s("А", 3.0, "1"), s("А", 4.0, "1"));
    }

    @Test
    void toStringContainsAllFields() {
        String text = s("ИТ-21-1", 4.0, "100001").toString();
        assertTrue(text.contains("ИТ-21-1"));
        assertTrue(text.contains("100001"));
    }
}
