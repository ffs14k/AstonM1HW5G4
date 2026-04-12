import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class StudentFileWriterService {

    private final String filePath;
    private final StudentCsvFormatter formatter = new StudentCsvFormatter(new StudentValidator());

    public StudentFileWriterService(String filePath) {
        this.filePath = filePath;
    }

    public void writeToFile(List<Student> students) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            for (Student student : students) {
                writer.write(formatter.format(student));
                writer.newLine();
            }
        }
    }
}
