import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ManualInputDataSource implements DataSourceStrategy<Student> {
    private final Scanner scanner;
    private final StudentCsvFormatter formatter;

    public ManualInputDataSource(Scanner scanner) {
        this.scanner = scanner;
        this.formatter = new StudentCsvFormatter(new StudentValidator());
    }

    @Override
    public List<Student> load() {
        List<Student> inputStudents = new ArrayList<>();
        System.out.println("Введите студентов в формате: группа,средний балл,номер зачётной книжки");
        System.out.println("Для завершения введите пустую строку");

        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                break;
            }

            try {
                Student student = formatter.parse(line);
                inputStudents.add(student);
                System.out.println("Студент добавлен");
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage() + ". Пропускаем этого студента");
            }
        }
        return inputStudents;
    }
}
