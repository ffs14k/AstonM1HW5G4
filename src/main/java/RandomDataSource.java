import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RandomDataSource implements DataSourceStrategy<Student> {
    private final int count;
    private final Random random;
    private final StudentValidator studentValidator = new StudentValidator();

    public RandomDataSource(int count) {
        this(count, new Random());
    }

    // второй конструктор нужен для тестов, чтобы можно было сделать одинаковую "рандомную" последовательность
    public RandomDataSource(int count, Random random) {
        this.count = count;
        this.random = random;
    }

    @Override
    public List<Student> load() {
        return IntStream.range(0, count)
                .mapToObj(s -> generateStudent())
                .filter(s -> {                                   // отсеиваем невалидных
                    try {
                        studentValidator.validate(s);            //если исключения нет, значит все верно
                        return true;
                    } catch (IllegalArgumentException e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    private Student generateStudent() {
        int course = 21 + random.nextInt(9);
        int groupNum = 1 + random.nextInt(9);
        String group = "ИТ" +"-" + course + "-" + groupNum;

        double average = random.nextDouble() * 5.0;  // от 0.0 до 5.0 (диапазон из StudentValidator)
        int recordNumber = 100_000 + random.nextInt(900_000);
        String recordBook = String.valueOf(recordNumber);

        return new Student.Builder()
                .group(group)
                .average(Math.round(average * 100.0) / 100.0) // округление
                .recordBook(recordBook)
                .build();
    }
}
