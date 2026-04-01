import java.util.List;

public interface DataSourceStrategy<T> {
    List<T> load();
}
