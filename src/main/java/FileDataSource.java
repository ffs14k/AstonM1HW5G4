import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileDataSource implements DataSourceStrategy<Student> {

    private final String filePath;
    private final StudentCsvFormatter formatter = new StudentCsvFormatter(new StudentValidator());

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
        return formatter.parse(line);
    }
}
