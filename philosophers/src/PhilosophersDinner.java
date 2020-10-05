public class PhilosophersDinner {
    private static final int PHILOSOPHERS_NUMBER = 5;
    private static final int PHILOSOPHERS_THINK_TIMER = 1000;
    private static final int PHILOSOPHERS_EAT_TIMER = 1000;

    public static void main(String[] args) {
        new DinnerWithSemaphore(PHILOSOPHERS_NUMBER, PHILOSOPHERS_THINK_TIMER, PHILOSOPHERS_EAT_TIMER);
    }
}
