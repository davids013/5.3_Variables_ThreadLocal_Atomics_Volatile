package task2v2;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class Main_task2v2 {

    private static final String MAIN_COLOR = "\u001b[0m";
    private static final String THREADS_COLOR = "\u001b[35m";
    private static final int STORES_QUANTITY = 3;           // Количество магазинов в сети
    private static final int ARRAY_SIZE = 100;              // Размер готового массива для отчёта
    private static final int MIN_PRICE = 50;                // Наименьшая цена проданных товаров
    private static final int MAX_PRICE = 2_000;             // Наибольшая цена проданных товаров

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println(MAIN_COLOR + "\n\tЗадача 2. Отчёт для налоговой\n" + THREADS_COLOR);

        final ExecutorService pool = Executors.newFixedThreadPool(STORES_QUANTITY);
        final AtomicLong sum = new AtomicLong(0);

        for (int i = 1; i <= STORES_QUANTITY; i++) {
            long temp = pool.submit(new RevenueSumTask(getIntRandomArray(ARRAY_SIZE))).get();
            System.out.printf("Выручка из %d-го магазина составляет %d (всего %d)\n", i, temp, sum.addAndGet(temp));
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