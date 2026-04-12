import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class OccurrencesCounterService {

    private static final int THREAD_COUNT = 4;

    public long countOccurrences(List<Student> list, Student target) {
        int size = list.size();
        if (size == 0) return 0;

        int chunkSize = Math.max(1, (size + THREAD_COUNT - 1) / THREAD_COUNT);
        AtomicLong total = new AtomicLong(0);
        List<Thread> threads = new ArrayList<>();

        for (int t = 0; t < THREAD_COUNT; t++) {
            final int from = t * chunkSize;
            if (from >= size) break;
            final int to = Math.min(from + chunkSize, size);

            Thread thread = new Thread(() -> {
                long count = 0;
                for (int i = from; i < to; i++) {
                    if (list.get(i).equals(target)) {
                        count++;
                    }
                }
                total.addAndGet(count);
            });
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("подсчёт прерван", e);
            }
        }

        return total.get();
    }
}
