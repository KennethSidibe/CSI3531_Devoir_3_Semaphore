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
    
    
    TaOfficeSimulator() {
        // TODO
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

        //TODO
        
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

        // TODO
        
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
