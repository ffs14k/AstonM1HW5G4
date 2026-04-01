public class StudentValidator {

    private static final double MIN_AVERAGE = 0.0;
    private static final double MAX_AVERAGE = 5.0;

    public void validate(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("студент не может быть null");
        }
        validateGroup(student.getGroup());
        validateAverage(student.getAverage());
        validateRecordBook(student.getRecordBook());
    }

    private void validateGroup(String group) {
        if (group == null || group.isBlank()) {
            throw new IllegalArgumentException("группа не может быть пустой");
        }
    }

    private void validateAverage(double average) {
        if (average < MIN_AVERAGE || average > MAX_AVERAGE) {
            throw new IllegalArgumentException(String.format("средний балл должен быть от %.1f до %.1f, получено: %.2f", MIN_AVERAGE, MAX_AVERAGE, average));
        }
    }

    private void validateRecordBook(String recordBook) {
        if (recordBook == null || recordBook.isBlank()) {
            throw new IllegalArgumentException("номер зачётной книжки не может быть пустым");
        }
    }
}
