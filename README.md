# Сортировка студентов — AstonM1HW5G4

## Формат CSV

average — от 0.0 до 5.0, group и recordBook — непустые строки.

```
group,average,recordBook
ИТ-21-1,4.5,100001
```

### Issue #1 — RandomDataSource + ManualInputDataSource

- Реализовать два класса, оба имплементируют DataSourceStrategy<Student>.
- Валидировать через StudentValidator.
- Коллекцию заполнять через стримы.
- Подключить оба в AppController (пункты 2 и 3 меню).
- Написать тесты

### Issue #2 — EvenOddDecorator

- Создать класс EvenOddDecorator<T> имплементирующий SortStrategy<T>. Логика: элементы с чётным значением поля сортируются делегатом, элементы с нечётным значением остаются на своих исходных позициях.
- Добавить в AppController вопрос после выбора поля сортировки. EvenOdd сортировка? yes/no. Если yes, выбрать even/odd.
- Написать тесты.

### Issue #3 — FileWriterService

- Создать класс FileWriterService.
- Добавить в AppController вопрос после вывода результата: сохранить в файл y/n.
- Написать тесты.

### Issue #4 — OccurrencesCounterService

- Метод countOccurrences(List<Student> list, Student target) считает количество вхождений target в list. Список разбивается на части, каждая часть обрабатывается в отдельном потоке, результаты суммируются.
- Добавить в AppController вопрос после вывода результата: найти вхождения yes/no.
- Написать тесты.
