package task2v3;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;

public class Main_task2v3 {
    private static final String RESET_COLOR = "\033[0m";
    private static final int STORES_QUANTITY = 3;           // Количество магазинов в сети
    private static final int ARRAY_SIZE = 100;              // Размер готового массива для отчёта
    private static final int MIN_PRICE = 50;                // Наименьшая цена проданных товаров
    private static final int MAX_PRICE = 2_000;             // Наибольшая цена проданных товаров

    public static void main(String[] args) {
        System.out.println(RESET_COLOR + "\n\tЗадача 2. Отчёт для налоговой\n");

        final ExecutorService pool = Executors.newFixedThreadPool(STORES_QUANTITY);
        final LongAdder sum = new LongAdder();

        for (int i = 1; i <= STORES_QUANTITY; i++)
            pool.submit(new RevenueSumTask(getIntRandomArray(ARRAY_SIZE), sum));

        pool.shutdown();
        while (!pool.isTerminated()) ;
        System.out.println(RESET_COLOR + "Общая дневная выручка сети магазинов составляет: " + sum.sum());
    }

    private static int[] getIntRandomArray(int size) {
        int[] array = new int[size];
        final Random rnd = new Random();
        for (int i = 0; i < array.length; i++)
            array[i] = rnd.nextInt(MIN_PRICE, MAX_PRICE + 1);
        return array;
    }
}

//record RevenueSumTask(int[] array, LongAdder longAdder) implements Runnable {
//    @Override
//    public void run() {
//        for (int i : array)
//            longAdder.add(i);
//    }
//}

class RevenueSumTask implements Runnable {
    private final int[] array;
    private final LongAdder longAdder;

    public RevenueSumTask(int[] array, LongAdder longAdder) {
        this.array = array;
        this.longAdder = longAdder;
    }

    @Override
    public void run() {
        for (int i : array)
            longAdder.add(i);
    }
}