import java.util.concurrent.Semaphore;

public class DinnerWithMonitor {

    public static int numberOfPhilosophers;
    public static PhilosopherWithMonitor[] philosophers;

    public DinnerWithMonitor(int numberOfPhilosophers, int thinkTimer, int eatTimer) {
        this.numberOfPhilosophers = numberOfPhilosophers;
        this.philosophers = new PhilosopherWithMonitor[numberOfPhilosophers];

        for (int i = 0; i < numberOfPhilosophers; i++)
            this.philosophers[i] = new PhilosopherWithMonitor(i, thinkTimer, eatTimer);
    }

    public static void take_cutlery(int philosopherId) {
        try {
            PhilosopherWithMonitor cPhilosopher = philosophers[philosopherId];
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
        PhilosopherWithMonitor cPhilosopher = philosophers[philosopherId];
        cPhilosopher.state = PhilosopherState.THINKING;

        if (canGiveCutleryRight(cPhilosopher.id)) {
            cPhilosopher = philosophers[getRightPhilosopherId(cPhilosopher.id)];
            cPhilosopher.state = PhilosopherState.EATING;
            synchronized (cPhilosopher) { cPhilosopher.notify(); }
        }
        if (canGiveCutleryLeft(cPhilosopher.id)) {
            cPhilosopher = philosophers[getLeftPhilosopherId(cPhilosopher.id)];
            cPhilosopher.state = PhilosopherState.EATING;
            synchronized (cPhilosopher) { cPhilosopher.notify(); }
        }
    }

    //=========================================

    private static boolean canEat(int philosopherId) {
        PhilosopherWithMonitor rPhilosopher = philosophers[getRightPhilosopherId(philosopherId)];
        PhilosopherWithMonitor lPhilosopher = philosophers[getLeftPhilosopherId(philosopherId)];
        return rPhilosopher.state != PhilosopherState.EATING && lPhilosopher.state != PhilosopherState.EATING;
    }

    private static boolean canGiveCutleryRight(int philosopherId) {
        PhilosopherWithMonitor rPhilosopher = philosophers[getRightPhilosopherId(philosopherId)];
        PhilosopherWithMonitor rrPhilosopher = philosophers[getRightPhilosopherId(rPhilosopher.id)];
        return rPhilosopher.state == PhilosopherState.REQUESTINGCUTLERY && rrPhilosopher.state != PhilosopherState.EATING;
    }

    private static boolean canGiveCutleryLeft(int philosopherId) {
        PhilosopherWithMonitor lPhilosopher = philosophers[getLeftPhilosopherId(philosopherId)];
        PhilosopherWithMonitor llPhilosopher = philosophers[getLeftPhilosopherId(lPhilosopher.id)];
        return lPhilosopher.state == PhilosopherState.REQUESTINGCUTLERY && llPhilosopher.state != PhilosopherState.EATING;
    }

    private static int getRightPhilosopherId(int philosopherId) {
        return (philosopherId + 1) % numberOfPhilosophers;
    }

    private static int getLeftPhilosopherId(int philosopherId) {
        return (philosopherId + numberOfPhilosophers - 1) % numberOfPhilosophers;
    }

}
