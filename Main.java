import java.util.Arrays;

public class Main {
    public static void main(String[] args) {


        TACoordinator testCoordinator = new TACoordinator();

        Student testStudent1 = new Student();
        Student testStudent2 = new Student();
        Student testStudent3 = new Student();

        testCoordinator.enqueue(testStudent1);
        testCoordinator.enqueue(testStudent2);

        testCoordinator.taNaps();
        try {
            Thread.sleep(1000);
            System.out.println("waking up TA \n");
        }catch (InterruptedException e) {
        }
        testCoordinator.wakeTaUp();

    }
}
