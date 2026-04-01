import java.util.Comparator;

public class ComparatorFactory {

    private ComparatorFactory() {}

    public static Comparator<Student> getComparator(int choice) {
        return switch (choice) {
            case 1 -> Comparator.comparing(Student::getGroup);
            case 2 -> Comparator.comparingDouble(Student::getAverage);
            case 3 -> Comparator.comparing(Student::getRecordBook);
            case 4 -> Comparator.comparing(Student::getGroup)
                    .thenComparingDouble(Student::getAverage)
                    .thenComparing(Student::getRecordBook);
            default -> throw new IllegalArgumentException("неверный номер поля: " + choice);
        };
    }
}
