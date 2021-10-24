package task2;

import java.util.Arrays;

public class Store {
    private static volatile int counter = 0;
    private final ThreadLocal<Long> ID = ThreadLocal.withInitial(() -> (long) ++counter);
    private final ThreadLocal<Long> revenue = ThreadLocal.withInitial(() -> 0L);

    public Store() {}

    public int getSum(int[] array) {
        return Arrays.stream(array).sum();
    }

    public long getRevenue() { return revenue.get(); }

    public void cell(int cost) {
        revenue.set(revenue.get() + cost);
        System.out.println(this + " продал товар стоимостью " + cost + " (выручка за день " + revenue.get() + ")");
    }

    @Override
    public String toString() { return "Магазин " + ID.get(); }
}
