public class StudentCsvFormatter {

    private static final String DELIMITER = ",";
    private static final int FIELD_COUNT = 3;
    private static final int GROUP_IDX = 0;
    private static final int AVERAGE_IDX = 1;
    private static final int RECORD_BOOK_IDX = 2;

    private final StudentValidator validator;

    public StudentCsvFormatter(StudentValidator validator) {
        this.validator = validator;
    }

    public String format(Student student) {
        validator.validate(student);
        return student.getGroup() + DELIMITER + student.getAverage() + DELIMITER + student.getRecordBook();
    }

    public Student parse(String line) {
        String[] parts = line.split(DELIMITER);
        if (parts.length != FIELD_COUNT) {
            throw new IllegalArgumentException("неверный формат строки (ожидается group,average,recordBook): " + line);
        }

        double average;
        try {
            average = Double.parseDouble(parts[AVERAGE_IDX].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("неверный формат среднего балла: " + parts[AVERAGE_IDX].trim());
        }

        Student student = new Student.Builder()
                .group(parts[GROUP_IDX].trim())
                .average(average)
                .recordBook(parts[RECORD_BOOK_IDX].trim())
                .build();
        validator.validate(student);
        return student;
    }
}
