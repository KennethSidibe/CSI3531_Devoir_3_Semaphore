import java.util.concurrent.locks.ReentrantLock;
import java.util.Random;
import java.util.UUID;

public class Student implements Runnable {

    private Thread studentProgrammingThread;
    private boolean isStudentProgrammingThreadRunning = false;
    private Thread runningThread;
    private final ReentrantLock lock = new ReentrantLock();
    private boolean studentProgramming = false;
    private boolean studentWaitingHelp = false;
    private String name;
    private UUID id = UUID.randomUUID();

    private TACoordinator taBrain;

    Student(String name, TACoordinator taBrain) {
        this.name = name;
        this.taBrain = taBrain;
        this.runningThread = new Thread(this);
    }

    Student(TACoordinator taBrain) {
        this.name = "pierre";
        this.taBrain = taBrain;
    }
 
    public void askForHelp() {
        
    }

    public String getName() {
        return this.name;
    }

    public synchronized boolean isStudentWaitingForHelp() {
        return this.studentWaitingHelp;
    }

    public synchronized boolean isStudentProgramming() {
        return this.studentProgramming;
    }

    public synchronized void waitForASpot() {

        System.out.println("Student" + name + " is now waiting for a spot ");
        
        while(!(this.taBrain.isSpotAvailable())) {
            programs();
        }

        System.out.println("Student" + name + " found a spot");

        taBrain.enqueue(this);
    }

    public UUID getStudentId() {
        return id;
    }

    public Thread getStudentThread() {
        return studentProgrammingThread;
    }

    public void programs() {

        lock.lock();
        try {
            studentProgramming = true;
        } finally {
            lock.unlock();
        }

        int maxSleepTime = 5000;
        int minSleepTime = 1000;
        int sleepTime = new Random().nextInt(maxSleepTime - minSleepTime) + minSleepTime;
        final int sleepIncrementConstant = 1000;

        studentProgrammingThread = new Thread(() -> {
            try {
                
                for (int i = 0; i <= sleepTime; i+=sleepIncrementConstant) {
                    System.out.println("student" + this.id + " is programming");
                    Thread.sleep(sleepIncrementConstant);
                }

                System.out.println("student " + this.id + " suspended programming");

                lock.lock();
                try {
                    studentProgramming = false;
                } finally {
                    lock.unlock();
                }
                
            }
            catch (InterruptedException e) {
                lock.lock();
                try {
                    studentProgramming = false;
                } finally {
                    lock.unlock();
                }
                System.out.println("student " + this.id + " stopped programming");
            }
        });

        studentProgrammingThread.start();

    }

    public void waitForHelp() {

        System.out.println("Student" + name + " is now waiting for TA helps ");

        while( taBrain.isTaHelpingStudent() ) {

            lock.lock(); 
            try {
                if(this.isStudentProgrammingThreadRunning) {
                    try {
                        this.isStudentProgrammingThreadRunning = true;
                        this.programs();
                    } catch(Exception e) {
                        System.out.println("exception caught : " + e);
                    }

                }
            } finally {
                lock.unlock();
            }
            
        }

        System.out.println("Student " + name +" can get help now");
    }

    public void leaves() {
        System.out.println("Student "+ name + " found help and leaves");
    }
    
    @Override
    public void run() {

        // uncomment the two things en bas

        // waitForASpot();

        waitForHelp();

        // leaves();

    }

    public void start() {

        runningThread.start();
    }


    

}