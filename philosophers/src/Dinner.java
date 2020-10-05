public abstract  class Dinner {

    public static int numberOfPhilosophers;
    public static Philosopher[] philosophers;

    public Dinner(int numberOfPhilosophers) {
        this.numberOfPhilosophers = numberOfPhilosophers;
        this.philosophers = new Philosopher[numberOfPhilosophers];
    }

    protected static boolean canEat(int philosopherId) {
        Philosopher rPhilosopher = philosophers[getRightPhilosopherId(philosopherId)];
        Philosopher lPhilosopher = philosophers[getLeftPhilosopherId(philosopherId)];
        return rPhilosopher.state != PhilosopherState.EATING && lPhilosopher.state != PhilosopherState.EATING;
    }

    protected static boolean canGiveCutleryRight(int philosopherId) {
        Philosopher rPhilosopher = philosophers[getRightPhilosopherId(philosopherId)];
        Philosopher rrPhilosopher = philosophers[getRightPhilosopherId(rPhilosopher.id)];
        return rPhilosopher.state == PhilosopherState.REQUESTINGCUTLERY && rrPhilosopher.state != PhilosopherState.EATING;
    }

    protected static boolean canGiveCutleryLeft(int philosopherId) {
        Philosopher lPhilosopher = philosophers[getLeftPhilosopherId(philosopherId)];
        Philosopher llPhilosopher = philosophers[getLeftPhilosopherId(lPhilosopher.id)];
        return lPhilosopher.state == PhilosopherState.REQUESTINGCUTLERY && llPhilosopher.state != PhilosopherState.EATING;
    }

    protected static int getRightPhilosopherId(int philosopherId) {
        return (philosopherId + 1) % numberOfPhilosophers;
    }

    protected static int getLeftPhilosopherId(int philosopherId) {
        return (philosopherId + numberOfPhilosophers - 1) % numberOfPhilosophers;
    }
}
