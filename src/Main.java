import task1.Main_task1;
import task2.Main_task2;

import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        System.out.println("""
                
                \tМодуль 5. Многопоточное программирование
                \tЛекция 3. Переменные многопоточное программы. ThreadLocal, Atomics, Volatile
                """);

        Main_task1.main(null);
        Main_task2.main(null);
    }
}
