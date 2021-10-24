package task1;

public class Main_task1 {

    /**
     * В текущей задаче не возникло проблем и без использования volatile
     * Что нужно было делать иначе для понимания проблемы?
     */

    private static final boolean EXTERNAL_END = true;       // Флаг остановки игрушки из главного потока
    private static final byte CYCLES = 4;                   // Количество включений игрушки игроком
    private static final short SWITCH_DELAY = 1_200;        // Время между включениями
    private static final String GAMER_COLOR = "\u001b[34m"; // Цвет печати в консоль действий игрока
    private static final String TOY_COLOR = "\u001b[31m";   // Цвет печати в консоль действий игрушки
    private static final String MAIN_COLOR = "\u001b[0m";   // Цвет печати в консоль действий главного потока
    private static
//                volatile
                            boolean status = false;         // Переменная, к которой обращаются потоки

    public static void main(String[] args) {
        System.out.println(MAIN_COLOR + "\n\tЗадача 1. Самая бесполезная коробка\n");

        Thread t1 = new Thread(() -> {
            for (byte i = 0; i < CYCLES; ) {
                if (Thread.currentThread().isInterrupted()) break;
                if (!status) {
                    System.out.println(GAMER_COLOR + Thread.currentThread().getName() + " включает игрушку");
                    status = true;
                    i++;
                    try {
                        Thread.sleep(SWITCH_DELAY);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Thread t2 = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                if (status) {
                    System.out.println(TOY_COLOR + Thread.currentThread().getName() + " выключает тумблер");
                    status = !status;
                }
                if (!EXTERNAL_END) {
                    if (t1.getState() == Thread.State.TERMINATED) break;
                }
            }
        });

        t1.setName("Игрок");
        t2.setName("Игрушка");

        t1.start();
        t2.start();

        if (!EXTERNAL_END) {
            while (t1.getState() != Thread.State.TERMINATED && t2.getState() != Thread.State.TERMINATED);
        } else {
            while (t1.getState() != Thread.State.TERMINATED);
            t2.interrupt();
        }
        System.out.println(MAIN_COLOR + "Конечное состояние игрушки: " + ((status) ? "ВКЛ." : "ВЫКЛ."));
    }
}
