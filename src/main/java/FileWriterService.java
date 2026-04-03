import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileWriterService {

    private final String filePath;

    public FileWriterService(String filePath) {
        this.filePath = filePath;
    }

    public void writeToFile(List<Student> students) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            for (Student student : students) {
                writer.write(formatCsv(student));
                writer.newLine();
            }
        }
    }

    String formatCsv(Student student) {
        return student.getGroup() + "," + student.getAverage() + "," + student.getRecordBook();
    }
}
