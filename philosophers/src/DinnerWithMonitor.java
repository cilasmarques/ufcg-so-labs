import java.util.concurrent.Semaphore;

public class DinnerWithMonitor extends  Dinner{

    public DinnerWithMonitor(int numberOfPhilosophers, int thinkTimer, int eatTimer) {
        super(numberOfPhilosophers);

        for (int i = 0; i < numberOfPhilosophers; i++)
            this.philosophers[i] = new PhilosopherWithMonitor(i, thinkTimer, eatTimer);
    }

    public static void take_cutlery(int philosopherId) {
        try {
            PhilosopherWithMonitor cPhilosopher = (PhilosopherWithMonitor) philosophers[philosopherId];
            cPhilosopher.state = PhilosopherState.REQUESTINGCUTLERY;

            if (canEat(cPhilosopher.id)) {  //tem talher disponivel?
                cPhilosopher.state = PhilosopherState.EATING;
            } else synchronized (cPhilosopher) {
                cPhilosopher.wait();        //espere até disponibilizar talher
            }

        } catch (InterruptedException e) {
            System.out.println("Interrupted Exception");
        }
    }

    public static void return_cutlery (int philosopherId) {
        PhilosopherWithMonitor cPhilosopher = (PhilosopherWithMonitor) philosophers[philosopherId];
        cPhilosopher.state = PhilosopherState.THINKING;

        if (canGiveCutleryRight(cPhilosopher.id)) {
            cPhilosopher = (PhilosopherWithMonitor) philosophers[getRightPhilosopherId(cPhilosopher.id)];
            cPhilosopher.state = PhilosopherState.EATING;
            synchronized (cPhilosopher) { cPhilosopher.notify(); }
        }
        if (canGiveCutleryLeft(cPhilosopher.id)) {
            cPhilosopher = (PhilosopherWithMonitor) philosophers[getLeftPhilosopherId(cPhilosopher.id)];
            cPhilosopher.state = PhilosopherState.EATING;
            synchronized (cPhilosopher) { cPhilosopher.notify(); }
        }
    }

}
