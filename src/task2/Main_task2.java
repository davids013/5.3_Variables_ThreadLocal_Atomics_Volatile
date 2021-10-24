package task2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Main_task2 {

    /**
     * Изначально делал просчёт выручки после совершения магазинами продаж.
     * А в задании речь, как я понял, про суммирование элементов готового массива.
     * Не уверен, что именно требовалось, поэтому оставил смену режима через переменную IS_ARRAY_GIVEN
      */

    private static final String MAIN_COLOR = "\u001b[0m";
    private static final String THREADS_COLOR = "\u001b[35m";
    private static final boolean IS_ARRAY_GIVEN = true;     // Расчёт по готовому массиву/после продаж
    private static final int STORES_QUANTITY = 5;           // Количество магазинов в сети
    private static final int ARRAY_SIZE = 100;              // Размер готового массива для отчёта
    private static final int BASE_SELL_TIME = 500;          // Периодичность продаж (при IS_ARRAY_GIVEN = false)
    private static final int WORKDAY_TIME = 3_000;          // Время работы магазинов (при IS_ARRAY_GIVEN = false)
    private static final int MIN_PRICE = 50;                // Наименьшая цена проданных товаров
    private static final int MAX_PRICE = 2_000;             // Наибольшая цена проданных товаров
    private static final int THREADS = Runtime.getRuntime().availableProcessors();  // Кол-во потоков пула

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        System.out.println(MAIN_COLOR + "\n\tЗадача 2. Отчёт для налоговой\n" + THREADS_COLOR);

        final ExecutorService pool = Executors.newFixedThreadPool(THREADS);
        final Store store = new Store();
        final Random rnd = new Random();
        final AtomicLong sum = new AtomicLong(0);

        if (IS_ARRAY_GIVEN) {
            final List<Callable<Integer>> tasks = new ArrayList<>();
            for (int i = 0; i < STORES_QUANTITY; i++) {
                tasks.add(() -> store.getSum(getIntRandomArray(ARRAY_SIZE)));
            }
            final List<Future<Integer>> futures = pool.invokeAll(tasks);
            final AtomicInteger counter = new AtomicInteger();
            futures.forEach((x) -> {
                try {
                    int temp = x.get();
                    sum.addAndGet(temp);
                    System.out.println("Дневная выручка магазина " + (counter.incrementAndGet()) + ": " + temp);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            });
        } else {
            for (int i = 0; i < STORES_QUANTITY; i++) {
                pool.submit(() -> {
                    final long startTime = System.currentTimeMillis();
                    while (!Thread.currentThread().isInterrupted()
                            && System.currentTimeMillis() - startTime < WORKDAY_TIME) {
                        store.cell(rnd.nextInt(MIN_PRICE, MAX_PRICE + 1));
                        try {
                            Thread.sleep(BASE_SELL_TIME + rnd.nextInt(0, BASE_SELL_TIME / 20));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    long temp = store.getRevenue();
                    sum.addAndGet(temp);
                    System.out.println(store + " выручил сегодня " + temp);
                });
            }
        }

        pool.shutdown();
        while (!pool.isTerminated());
        System.out.println(MAIN_COLOR + "Общая дневная выручка сети магазинов составляет: " + sum);
    }

    private static int[] getIntRandomArray(int size) {
        int[] array = new int[size];
        final Random rnd = new Random();
        for (int i = 0; i < array.length; i++) {
            array[i] = rnd.nextInt(MIN_PRICE, MAX_PRICE + 1);
        }
        return array;
    }
}
