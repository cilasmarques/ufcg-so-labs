import java.util.concurrent.Semaphore;

public class DinnerWithSemaphore {
    public static int numberOfPhilosophers;
    public static Semaphore philosopherAction;
    public static PhilosopherWithSemaphore[] philosophers;

    public DinnerWithSemaphore(int numberOfPhilosophers, int thinkTimer, int eatTimer) {
        this.numberOfPhilosophers = numberOfPhilosophers;
        this.philosopherAction = new Semaphore(1);
        this.philosophers = new PhilosopherWithSemaphore[numberOfPhilosophers];

        for (int i = 0; i < numberOfPhilosophers; i++)
            this.philosophers[i] = new PhilosopherWithSemaphore(i, thinkTimer, eatTimer);
    }

    public static void take_cutlery(int philosopherId) {
        try {
            philosopherAction.acquire(); //posso executar ? sim : nao
            PhilosopherWithSemaphore cPhilosopher = philosophers[philosopherId];
            cPhilosopher.state = PhilosopherState.REQUESTINGCUTLERY;

            if (canEat(cPhilosopher.id)) { //pode pegar o talher?
                cPhilosopher.eating.release(); //libera para executar
                cPhilosopher.state = PhilosopherState.EATING;
            }

            philosopherAction.release(); //libera para o proximo
            cPhilosopher.eating.acquire(); //posso executar ? sim : nao

        } catch (InterruptedException e) {
            System.out.println("Interrupted Exception");
        }
    }

    public static void return_cutlery (int philosopherId) {
        try {
            philosopherAction.acquire(); //alguem executando ? espera : executa
            PhilosopherWithSemaphore cPhilosopher = philosophers[philosopherId];
            cPhilosopher.state = PhilosopherState.THINKING;

            if (canGiveCutleryRight(cPhilosopher.id)) {
                cPhilosopher = philosophers[getRightPhilosopherId(cPhilosopher.id)];
                cPhilosopher.state = PhilosopherState.EATING;
                cPhilosopher.eating.release();
            }
            if (canGiveCutleryLeft(cPhilosopher.id)) {
                cPhilosopher = philosophers[getLeftPhilosopherId(cPhilosopher.id)];
                cPhilosopher.state = PhilosopherState.EATING;
                cPhilosopher.eating.release();
            }

            philosopherAction.release();

        } catch (InterruptedException e) {
            System.out.println("Interrupted Exception");
        }
    }

    //====================================================

    private static boolean canEat(int philosopherId) {
        PhilosopherWithSemaphore rPhilosopher = philosophers[getRightPhilosopherId(philosopherId)];
        PhilosopherWithSemaphore lPhilosopher = philosophers[getLeftPhilosopherId(philosopherId)];
        return rPhilosopher.state != PhilosopherState.EATING && lPhilosopher.state != PhilosopherState.EATING;
    }

    private static boolean canGiveCutleryRight(int philosopherId) {
        PhilosopherWithSemaphore rPhilosopher = philosophers[getRightPhilosopherId(philosopherId)];
        PhilosopherWithSemaphore rrPhilosopher = philosophers[getRightPhilosopherId(rPhilosopher.id)];
        return rPhilosopher.state == PhilosopherState.REQUESTINGCUTLERY && rrPhilosopher.state != PhilosopherState.EATING;
    }

    private static boolean canGiveCutleryLeft(int philosopherId) {
        PhilosopherWithSemaphore lPhilosopher = philosophers[getLeftPhilosopherId(philosopherId)];
        PhilosopherWithSemaphore llPhilosopher = philosophers[getLeftPhilosopherId(lPhilosopher.id)];
        return lPhilosopher.state == PhilosopherState.REQUESTINGCUTLERY && llPhilosopher.state != PhilosopherState.EATING;
    }

    private static int getRightPhilosopherId(int philosopherId) {
        return (philosopherId + 1) % numberOfPhilosophers;
    }

    private static int getLeftPhilosopherId(int philosopherId) {
        return (philosopherId + numberOfPhilosophers - 1) % numberOfPhilosophers;
    }

}