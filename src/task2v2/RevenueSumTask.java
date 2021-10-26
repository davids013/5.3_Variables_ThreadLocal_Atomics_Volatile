package task2v2;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.LongAdder;

public class RevenueSumTask implements Callable<Long> {
    private final int[] array;
    private final LongAdder longAdder;

    public RevenueSumTask(int[] array) {
        this.array = array;
        longAdder = new LongAdder();
    }

    @Override
    public Long call() {
        for (int i : array) {
            longAdder.add(i);
        }
        return longAdder.sum();
    }
}
