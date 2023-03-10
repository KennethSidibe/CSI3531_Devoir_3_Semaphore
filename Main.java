import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        TeacherAssistant tAssistant = new TeacherAssistant();
        Student[] studentsList = TaOfficeSimulator.
                            generateStudentsList(10);

        TACoordinator taBrain = new TACoordinator(tAssistant, studentsList);
        
        System.out.println(Arrays.deepToString(taBrain.getAllStudentsToHelpName()));
        
        System.out.println("studentsRemainingFlag: "+taBrain.areStudentsToHelpRemaining()+"\n\n");

        
        taBrain.start();

        try {
            Thread.sleep(2000);
            System.out.println(Arrays.deepToString(taBrain.getAllStudentsInWaitRoomName()));
            System.out.println("Original wait room");
        } catch (Exception e) {}

        System.out.println("\n");

        try {
            Thread.sleep(5000);
            System.out.println("dequeuing next student \n");
            taBrain.dequeue();
        } catch (Exception e) {}


        System.out.println("\n");

        try {
            Thread.sleep(2000);
            System.out.println("Updated wait room \n");
            System.out.println(Arrays.deepToString(taBrain.getAllStudentsInWaitRoomName()));
        } catch (Exception e) {}

        System.out.println("\n");
        

        try {
            Thread.sleep(5000);
            System.out.println("dequeuing next student \n");
            taBrain.dequeue();
        } catch (Exception e) {}

        System.out.println("\n");


        try {
            Thread.sleep(2000);
            System.out.println("Updated wait room \n");
            System.out.println(Arrays.deepToString(taBrain.getAllStudentsInWaitRoomName()));
        } catch (Exception e) {}

        System.out.println("\n");

        taBrain.interruptFillThread();
        
        taBrain.interruptTaCoordinatorThread();
        

    }
}
