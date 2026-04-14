import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class AppController {

    private static final String DEFAULT_FILE = "data/students.csv";
    private static final int MAX_RANDOM_STUDENTS = 50;  // ограничение кол-ва случайных студентов
    private final Scanner scanner = new Scanner(System.in);

    public void run() {
        List<Student> students = null;
        String path = DEFAULT_FILE;

        System.out.println("Сортировка студентов");
        System.out.println("--------------------");

        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("Источник данных:");
            System.out.println("  3 - ручной ввод");
            System.out.println("  2 - случайные данные");
            System.out.println("  1 - файл");
            System.out.println("  0 - выйти");
            System.out.print("> ");
            int source = readInt();

            if (source == 0) {
                running = false;
                continue;
            }
            else if (source == 1) {
                System.out.print("Путь к файлу (Enter = " + DEFAULT_FILE + "): ");
                 path = scanner.nextLine().trim();
                if (path.isEmpty()) path = DEFAULT_FILE;
                students = loadStudents(path);
                if (students == null) continue;
                System.out.println("Загружено " + students.size() + " студентов.");
            }
            else if (source==2){
                int count = readIntInRange(1, MAX_RANDOM_STUDENTS, "Количество студентов (1-" + MAX_RANDOM_STUDENTS + "): ");
                RandomDataSource ds = new RandomDataSource(count);
                students = ds.load();
                if (students.isEmpty()) {
                    System.out.println("Не удалось сгенерировать ни одного валидного студента. Попробуйте другой источник");
                    continue;   // возврат к выбору источника
                }
                System.out.println("Сгенерировано " + students.size() + " студентов.");
            }
            else if (source==3){
                ManualInputDataSource ds = new ManualInputDataSource(scanner);
                students = ds.load();
                if (students.isEmpty()) {
                    System.out.println("Не введено ни одного валидного студента. Попробуйте другой источник");
                    continue;
                }
                System.out.println("Введено " + students.size() + " студентов.");
            }
            else {
                System.out.println("Некорректный ввод: Введите число от 0 до 3");
                continue;
            }


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
    private int readIntInRange(int min, int max, String s) {
        //Валидация ввода числа рандомных студентов
        while (true) {
            System.out.print(s);
            int value = readInt();
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println("Ошибка: введите число от " + min + " до " + max);
        }
    }
}
