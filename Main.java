import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.out.println("This main method will be the test playground");


        TACoordinator testCoordinator = new TACoordinator();

        Student testStudent1 = new Student();
        Student testStudent2 = new Student();
        Student testStudent3 = new Student();

        testCoordinator.enqueue(testStudent1);
        testCoordinator.enqueue(testStudent2);
        testCoordinator.enqueue(testStudent3);

        testCoordinator.enqueue(testStudent3);

        System.out.println(Arrays.deepToString(testCoordinator.line));

        testCoordinator.dequeue();
        testCoordinator.dequeue();
        testCoordinator.dequeue();
        
        testCoordinator.dequeue();

        System.out.println(Arrays.deepToString(testCoordinator.line));


    }
}
