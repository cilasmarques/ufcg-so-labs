import java.util.concurrent.Semaphore;

public class PhilosopherWithMonitor implements Runnable {

    public int id;
    public int thinkTimer;
    public int eatTimer;
    public PhilosopherState state;

    public PhilosopherWithMonitor(int id, int thinkTimer, int eatTimer) {
        this.id = id;
        this.thinkTimer = thinkTimer;
        this.eatTimer = eatTimer;
        this.state = PhilosopherState.THINKING;
        new Thread((Runnable)this, "Philosopher" + id).start();
    }

    @Override
    public void run() {
        try {
            while(true) {
                System.out.println("Philosopher " + this.id + " is thinking");
                Thread.sleep(this.thinkTimer);
                System.out.println("Philosopher " + this.id + " is requesting cutlery");
                DinnerWithMonitor.take_cutlery(this.id);
                System.out.println("Philosopher " + this.id + " is eating");
                Thread.sleep(this.eatTimer);
                System.out.println("Philosopher " + this.id + " finished eating");
                DinnerWithMonitor.return_cutlery(this.id);
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted Exception");
        }
    }

}
