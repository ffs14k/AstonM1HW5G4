import java.util.Objects;

public class Student {

    private final String group;
    private final double average;
    private final String recordBook;

    private Student(Builder builder) {
        this.group = builder.group;
        this.average = builder.average;
        this.recordBook = builder.recordBook;
    }

    public String getGroup() {
        return group;
    }

    public double getAverage() {
        return average;
    }

    public String getRecordBook() {
        return recordBook;
    }

    @Override
    public String toString() {
        return String.format("группа=%-8s  балл=%.2f  книжка=%s", group, average, recordBook);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student other)) return false;
        return Double.compare(other.average, average) == 0
                && Objects.equals(group, other.group)
                && Objects.equals(recordBook, other.recordBook);
    }

    @Override
    public int hashCode() {
        return Objects.hash(group, average, recordBook);
    }

    public static class Builder {
        private String group;
        private double average;
        private String recordBook;

        public Builder group(String group) {
            this.group = group;
            return this;
        }

        public Builder average(double average) {
            this.average = average;
            return this;
        }

        public Builder recordBook(String recordBook) {
            this.recordBook = recordBook;
            return this;
        }

        public Student build() {
            return new Student(this);
        }
    }
}
