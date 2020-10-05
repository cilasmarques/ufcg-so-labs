import java.util.concurrent.Semaphore;

public class PhilosopherWithSemaphore implements Runnable {

    public int id;
    public int thinkTimer;
    public int eatTimer;
    public PhilosopherState state;
    public Semaphore eating;

    public PhilosopherWithSemaphore(int id, int thinkTimer, int eatTimer) {
        this.id = id;
        this.thinkTimer = thinkTimer;
        this.eatTimer = eatTimer;
        this.eating = new Semaphore(0);
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
                DinnerWithSemaphore.take_cutlery(this.id);
                System.out.println("Philosopher " + this.id + " is eating");
                Thread.sleep(this.eatTimer);
                System.out.println("Philosopher " + this.id + " finished eating");
                DinnerWithSemaphore.return_cutlery(this.id);
            }
        } catch (InterruptedException e) {
                System.out.println("Interrupted Exception");
        }
    }

}

