import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class AppController {

    private static final String DEFAULT_FILE = "data/students.csv";

    private final Scanner scanner = new Scanner(System.in);

    public void run() {
        System.out.println("Сортировка студентов");
        System.out.println("--------------------");

        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("Источник данных:");
            System.out.println("  1 - файл");
            System.out.println("  0 - выйти");
            System.out.print("> ");
            int source = readInt();

            if (source == 0) {
                running = false;
                continue;
            }
            if (source != 1) {
                System.out.println("Неверный выбор, попробуй ещё раз");
                continue;
            }

            System.out.print("Путь к файлу (Enter = " + DEFAULT_FILE + "): ");
            String path = scanner.nextLine().trim();
            if (path.isEmpty()) path = DEFAULT_FILE;

            List<Student> students = loadStudents(path);
            if (students == null) continue;

            System.out.println("Загружено " + students.size() + " студентов.");

            System.out.println();
            System.out.println("Поле для сортировки:");
            System.out.println("  1 - группа");
            System.out.println("  2 - средний балл");
            System.out.println("  3 - зачётная книжка");
            System.out.println("  4 - все поля");
            System.out.print("> ");
            int field = readInt();

            Comparator<Student> comparator;
            try {
                comparator = ComparatorFactory.getComparator(field);
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage());
                continue;
            }

            SortStrategy<Student> sort = new BubbleSort<>();


            if (field== 2  || field == 4) {
                System.out.print("Применить EvenOdd сортировку (только чётные средние баллы)? (y/n): ");
                if (readYesNoChoice()) {
                    sort = new EvenOddDecorator(sort);
                }

            }

            sort.sort(students,comparator);



            System.out.println();
            for (int i = 0; i < students.size(); i++) {
                System.out.printf("  %2d. %s%n", i + 1, students.get(i));
            }

            System.out.print("\nНайти вхождения элемента? (y/n): ");
            if (readYesNoChoice()) {
                runCountOccurrencesFlow(students);
            }

            System.out.print("\nСохранить результат в CSV? (y/n): ");
            if (readYesNoChoice()) {
                runSaveToCSVFlow(students, path);
            }

            System.out.print("\nЕщё раз? (y/n): ");
            if (!readYesNo()) running = false;
        }

        System.out.println("Пока!");
    }

    private List<Student> loadStudents(String path) {
        List<Student> students;
        try {
            students = new FileDataSource(path).load();
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
            return null;
        }
        if (students.isEmpty()) {
            System.out.println("Файл загружен, но список пустой");
            return null;
        }
        return students;
    }

    private void runCountOccurrencesFlow(List<Student> students) {
        System.out.print("  введи студента (group,average,recordBook): ");
        String input = scanner.nextLine().trim();
        Student target;
        try {
            target = new StudentCsvFormatter(new StudentValidator()).parse(input);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
            return;
        }
        long count = new OccurrencesCounterService().countOccurrences(students, target);
        System.out.println("Вхождений найдено: " + count);
    }

    private void runSaveToCSVFlow(List<Student> students, String defaultPath) {
        System.out.print("Путь для сохранения (Enter = " + defaultPath + "): ");
        String outputPath = scanner.nextLine().trim();
        if (outputPath.isEmpty()) outputPath = defaultPath;
        try {
            new StudentFileWriterService(outputPath).writeToFile(students);
            System.out.println("Сохранено в: " + outputPath);
        } catch (Exception e) {
            System.out.println("Ошибка записи: " + e.getMessage());
        }
    }

    private int readInt() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Введи целое число");
            return -1;
        }
    }

    private boolean readYesNo() {
        String s = scanner.nextLine().trim().toLowerCase();
        // Для повтора приложения считаем "да" только явный y.
        return s.equals("y");
    }

    private boolean readYesNoChoice() {
        // Валидируем ввод, пока пользователь не введет y/n.
        while (true) {
            String s = scanner.nextLine().trim().toLowerCase();
            if (s.equals("y")) return true;
            if (s.equals("n")) return false;
            System.out.print("Введите y или n: ");
        }
    }
}
