import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EvenOddDecorator implements SortStrategy<Student>{
    private final  SortStrategy<Student> sortStrategy;

    public EvenOddDecorator(SortStrategy<Student> sortStrategy) {
        if (sortStrategy == null) {
            throw new IllegalArgumentException("SortStrategy cannot be null"); // ну вдруг
        }
        this.sortStrategy = sortStrategy;
    }

    @Override
    public void sort(List<Student> list, Comparator<Student> comparator) {

        if (list == null || list.size() < 2) return;
        List<Integer> evenIntegersIndexes = new ArrayList<>();   // ИМХО дабблы некорректно сортировать по четности, т.к. это х-ка целых чисел.
        for (int i = 0; i < list.size(); i++) {
            Student s =list.get(i);
            int intPart = (int)s.getAverage();           // поэтому тут отбрасывается дробная часть.

            if (intPart%2==0){                           // отбор индексов четных чисел
                evenIntegersIndexes.add(i);
            }
        }

        List<Student> toBeSorted = new ArrayList<>();   // добавляем студентов по индексу
        for (int index:evenIntegersIndexes){            // тк компаратор принимает студентов
            toBeSorted.add(list.get(index));
        }

        sortStrategy.sort(toBeSorted, comparator);         // сортировка (переданная)

        for (int i = 0; i < evenIntegersIndexes.size(); i++) {       // вставка на индекс числа из отсортированного листа
            list.set(evenIntegersIndexes.get(i), toBeSorted.get(i));
        }
    }
}
