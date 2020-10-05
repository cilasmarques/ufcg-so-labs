import java.util.Scanner;

public class MainPhilosophersDinner {
    private static final int PHILOSOPHERS_NUMBER = 5;
    private static final int PHILOSOPHERS_THINK_TIMER = 1000;
    private static final int PHILOSOPHERS_EAT_TIMER = 1000;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Choose the solution: 1-semaphore | 2-monitor");
        int option = sc.nextInt();

        if (option == 1)
            new DinnerWithSemaphore(PHILOSOPHERS_NUMBER, PHILOSOPHERS_THINK_TIMER, PHILOSOPHERS_EAT_TIMER);
        else if (option == 2)
            new DinnerWithMonitor(PHILOSOPHERS_NUMBER, PHILOSOPHERS_THINK_TIMER, PHILOSOPHERS_EAT_TIMER);
        else {
            System.out.println("Invalid option. Try again.");
            System.exit(1);
        }

        sc.close();
    }
}
