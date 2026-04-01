import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileDataSource implements DataSourceStrategy<Student> {

    private static final String DELIMITER = ",";
    private static final int FIELD_COUNT = 3;
    private static final int GROUP_IDX = 0;
    private static final int AVERAGE_IDX = 1;
    private static final int RECORD_BOOK_IDX = 2;

    private final String filePath;
    private final StudentValidator validator = new StudentValidator();

    public FileDataSource(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Student> load() {
        List<Student> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    result.add(parseLine(line));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("ошибка чтения файла: " + filePath, e);
        }
        return result;
    }

    private Student parseLine(String line) {
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
