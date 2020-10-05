import java.util.concurrent.Semaphore;

public class DinnerWithSemaphore extends Dinner {

    public static Semaphore philosopherAction;

    public DinnerWithSemaphore(int numberOfPhilosophers, int thinkTimer, int eatTimer) {
        super(numberOfPhilosophers);
        this.philosopherAction = new Semaphore(1);

        for (int i = 0; i < numberOfPhilosophers; i++)
            this.philosophers[i] = new PhilosopherWithSemaphore(i, thinkTimer, eatTimer);
    }

    public static void take_cutlery(int philosopherId) {
        try {
            philosopherAction.acquire(); //posso executar ? sim : nao
            PhilosopherWithSemaphore cPhilosopher = (PhilosopherWithSemaphore) philosophers[philosopherId];
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
            PhilosopherWithSemaphore cPhilosopher = (PhilosopherWithSemaphore) philosophers[philosopherId];
            cPhilosopher.state = PhilosopherState.THINKING;

            if (canGiveCutleryRight(cPhilosopher.id)) {
                cPhilosopher = (PhilosopherWithSemaphore) philosophers[getRightPhilosopherId(cPhilosopher.id)];
                cPhilosopher.state = PhilosopherState.EATING;
                cPhilosopher.eating.release();
            }
            if (canGiveCutleryLeft(cPhilosopher.id)) {
                cPhilosopher = (PhilosopherWithSemaphore) philosophers[getLeftPhilosopherId(cPhilosopher.id)];
                cPhilosopher.state = PhilosopherState.EATING;
                cPhilosopher.eating.release();
            }

            philosopherAction.release();

        } catch (InterruptedException e) {
            System.out.println("Interrupted Exception");
        }
    }

}