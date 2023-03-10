/**
 * TaOfficeSimulator
 */
import java.util.Arrays;
import java.util.Random;

public class TaOfficeSimulator implements Runnable {

    private int studentNumber;
    private Student[] students;
    private Thread[] studentThreads;
    private TACoordinator taBrain;
    
    
    TaOfficeSimulator(int studentNumber) {

        this.studentNumber = studentNumber;
        this.students = new Student[studentNumber];
        this.studentThreads = new Thread[studentNumber];
        this.taBrain = new TACoordinator();
        
    }

    public static String generateRandomName() {

        String[] nameRandom = {"Pierre", "Marc", "Jean", "Sophie", "Julie", "Jeanne", "Preston",
                                    "Franck", "Sarah", "Joveline", "Joanne", "Audrey", "Carine", "Ines",
                                "Moise", "Caleb", "Carlos", "Carole", "Robert", "Luck", "Joliane", "Angela",
                                "Francoise", "Ashley"};
        
        int randomNumber = new Random().nextInt(nameRandom.length-1 - 0) + 0;
        String randomName = nameRandom[randomNumber];

        return randomName;

    }

    public void simulate() {

        for (int i = 0; i < students.length; i++) {

            Student newStudent = new Student();

            students[i] = newStudent;
            studentThreads[i] = newStudent.getStudentThread();
        }
        
    }

    public static Student[] generateStudentsList(int numberStudentsToGenerate) {

        Student[] studentsList = new Student[numberStudentsToGenerate];

        for (int i = 0; i < studentsList.length; i++) {
            studentsList[i] = new Student();
        }
        
        return studentsList;
    }

    @Override
    public void run() {

    }

    public void start() { 

        System.out.println("here are all the students : "+Arrays.deepToString(getAllStudentName()));
        System.out.println();

        System.out.println("here are all the students that are in the waiting room : "+Arrays.deepToString(getTaBrain().getAllStudentsInWaitRoomName()));
        System.out.println();

        System.out.println("starting office\n\n");

        taBrain.start();

        this.run();
    }

    public TACoordinator getTaBrain() {
        return this.taBrain;
    }

    public String[] getAllStudentName() {
        String[] nameList = new String[students.length];

        for (int i = 0; i < nameList.length; i++) {
            Student student = students[i];
            nameList[i] = student.getName();
        }

        return nameList;
    }
    
}
