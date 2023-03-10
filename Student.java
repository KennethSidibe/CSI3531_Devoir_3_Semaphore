import java.util.concurrent.locks.ReentrantLock;
import java.util.Random;
import java.util.UUID;

public class Student implements Runnable {

    private final ReentrantLock lock = new ReentrantLock();
    private Thread studentProgrammingThread;
    private Thread runningThread;
    
    private boolean isStudentProgrammingThreadRunning = false;
    private boolean studentProgramming = false;
    private boolean studentCanGetHelp = false;

    private String name;
    private UUID id = UUID.randomUUID();

    Student(String name) {
        this.name = name;
        this.runningThread = new Thread(this);
    }

    Student() {
        this.name = TaOfficeSimulator.generateRandomName();
        this.runningThread = new Thread(this);
    }
 
    public void askForHelp() {
        
    }

    public String getName() {
        return this.name;
    }

    public synchronized boolean isStudentWaitingForHelp() {
        return this.studentCanGetHelp;
    }

    public synchronized boolean isStudentProgramming() {
        return this.studentProgramming;
    }

    public synchronized void endWait() {
        studentCanGetHelp = true;
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
                    System.out.println("student" + this.name + " is programming");
                    Thread.sleep(sleepIncrementConstant);
                }

                System.out.println("student " + this.name + " stopped programming");

                lock.lock();
                try {
                    studentProgramming = false;
                    this.isStudentProgrammingThreadRunning= false;
                } finally {
                    lock.unlock();
                }
                
            }
            catch (InterruptedException e) {
                lock.lock();
                try {
                    studentProgramming = false;
                    this.isStudentProgrammingThreadRunning= false;
                } finally {
                    lock.unlock();
                }
                System.out.println("student " + this.name + " suspended programming");
            }
        });

        studentProgrammingThread.start();
    }


    public synchronized boolean canStudentGetHelp() {
        return studentCanGetHelp;
    }

    public void waitForHelp() {

        System.out.println("Student" + name + " is waiting for Ta helps \n");

        while( !(canStudentGetHelp()) ) {

            lock.lock(); 
            try {
                if(this.isStudentProgrammingThreadRunning == false) {
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

        studentProgrammingThread.interrupt();
        System.out.println("Student " + name +" can get help now\n\n");
    }

    public void leaves() {
        System.out.println("Student "+ name + " found help and leaves");
    }
    
    @Override
    public void run() {

        

    }

    public void start() {

        runningThread.start();
    }


    

}