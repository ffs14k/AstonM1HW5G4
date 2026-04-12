import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EvenOddDecoratorTest {

    private static SortStrategy<Student> sortStrategy;
    private static Student s1, s2, s3, s4, s5, s6;

    @BeforeAll // создаю сразу студентов для всех тестов
    static void setUp() {
        sortStrategy = new BubbleSort<>();
        s1 = new Student.Builder().group("Г-11").average(2.0).recordBook("100001").build();
        s2 = new Student.Builder().group("Б-22").average(4.8).recordBook("100002").build();
        s3 = new Student.Builder().group("Г-11").average(4.5).recordBook("100003").build();

        s4 = new Student.Builder().group("Г-44").average(3.2).recordBook("100004").build();
        s5 = new Student.Builder().group("Б-55").average(3.9).recordBook("100005").build();
        s6 = new Student.Builder().group("Г-66").average(5.0).recordBook("100006").build();

    }

    @Test // пустой список: ничего не происходит
    public  void testEmptyList(){
        List<Student> students = new ArrayList<>(); // пустой лист

        EvenOddDecorator decorator = new EvenOddDecorator(sortStrategy);
        decorator.sort(students, Comparator.comparingDouble(Student::getAverage));

        assertTrue(students.isEmpty(), "Пустой список должен остаться пустым");
    }

    @Test  // список из 1 студента: ничего не происходит
    void testSingleElementList() {

        List<Student> students = new ArrayList<>();
        students.add(s1);
        List<Student> expected = new ArrayList<>(students);

        EvenOddDecorator decorator = new EvenOddDecorator(sortStrategy);
        decorator.sort(students, Comparator.comparingDouble(Student::getAverage));

        assertEquals(expected, students, "Список с одним элементом не должен измениться");
    }

    @Test  // все четные
    void testAllEven() {

        List<Student> students = new ArrayList<>();
        students.add(s1);
        students.add(s2);
        students.add(s3);

        List<Student> expected = new ArrayList<>();
        expected.add(s1);
        expected.add(s3);
        expected.add(s2);

        EvenOddDecorator decorator = new EvenOddDecorator(sortStrategy);
        decorator.sort(students, Comparator.comparingDouble(Student::getAverage));

        assertEquals(expected, students);
    }

    @Test // все нечетные: ничего не происходит
    void testAllOdd() {
        List<Student> students = new ArrayList<>();
        students.add(s4);
        students.add(s5);
        students.add(s6);
        List<Student> expected = new ArrayList<>(students); // порядок не меняется

        EvenOddDecorator decorator = new EvenOddDecorator(sortStrategy);
        decorator.sort(students, Comparator.comparingDouble(Student::getAverage));

        assertEquals(expected, students);
    }

    @Test // смешанное + сортировка по average
    void testMixed() {
        List<Student> students = new ArrayList<>();
        students.add(s1); // чёт 2.0
        students.add(s4); // нечёт 3.2
        students.add(s2); // чёт 4.8
        students.add(s5); // нечёт 3.9
        students.add(s3); // чёт 4.5

        List<Student> expected = new ArrayList<>();
        expected.add(s1);
        expected.add(s4);
        expected.add(s3);
        expected.add(s5);
        expected.add(s2);

        EvenOddDecorator decorator = new EvenOddDecorator(sortStrategy);
        decorator.sort(students, Comparator.comparingDouble(Student::getAverage));

        assertEquals(expected, students);
    }

    @Test // сортировка по 3 полям
    void testEvenOddWithThreeFieldsComparator() {
        Comparator<Student> threeFieldsComp = Comparator
                .comparing(Student::getGroup)
                .thenComparingDouble(Student::getAverage)
                .thenComparing(Student::getRecordBook);

        List<Student> students = new ArrayList<>(Arrays.asList(s1, s4, s3, s5, s2, s6));

        // чётные должны быть отсортированы как s2, s1, s3; s4-6 на местах

        List<Student> expected = new ArrayList<>(Arrays.asList(s2, s4, s1, s5, s3, s6));

        EvenOddDecorator decorator = new EvenOddDecorator(sortStrategy);
        decorator.sort(students, threeFieldsComp);

        assertEquals(expected, students);
    }

}
