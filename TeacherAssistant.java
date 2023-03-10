import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class TeacherAssistant {
    
    private ReentrantLock lock = new ReentrantLock();

    private String name;
    private boolean napping = false;
    private boolean helping = false;
    private boolean roomEmpty = true;

    private Thread napThread;
    private Thread helpThread;

    private boolean napThreadRunning = false;
    private boolean helpThreadRunning = false;

    TeacherAssistant() {
        this.name = TaOfficeSimulator.generateRandomName();
    }
    
    TeacherAssistant(String name) {
        this.name = name;
    }

    public void taNaps() {
        
        int maxSleepTime = 5000;
        int minSleepTime = 1000;
        int sleepTime = new Random().nextInt(maxSleepTime - minSleepTime) + minSleepTime;
        final int sleepIncrementConstant = 1000;

        napThread = new Thread(() -> {
            try {

                for (int i = 0; i <= sleepTime; i+=sleepIncrementConstant) {
                    System.out.println(name+":Zzz");
                    Thread.sleep(sleepIncrementConstant);
                }

                System.out.println("Ta finishes napping");
                lock.lock();
                try {
                    napping = false;
                    napThreadRunning = false;
                } finally {
                    lock.unlock();
                }
                
            }
            catch (InterruptedException e) {

                lock.lock();
                try {
                    napping = false;
                    napThreadRunning = false;
                } finally {
                    lock.unlock();
                }

                System.out.println("ta,"+name+" got waked up");
            }
        });

        lock.lock();
        try {
            napping = true;
            napThreadRunning = true;
        } finally {
            lock.unlock();
        }

        napThread.start();
    }

    public void taHelps(Student student) {

        int maxSleepTime = 5000;
        int minSleepTime = 1000;
        int sleepTime = new Random().nextInt(maxSleepTime - minSleepTime) + minSleepTime;
        final int sleepIncrementConstant = 1000;

        helpThread = new Thread(() -> {
            try {

                for (int i = 0; i <= sleepTime; i+=sleepIncrementConstant) {
                    System.out.println("Ta,"+name+" is helping student, " + student.getName());
                    Thread.sleep(sleepIncrementConstant);
                }

                System.out.println("Ta,"+name+" finished helping student " + student.getName());
                
                lock.lock();
                try {
                    helping = false;
                    helpThreadRunning = false;
                } finally {
                    lock.unlock();
                }
                
            }
            catch (InterruptedException e) {
                lock.lock();
                try {
                    helping = false;
                    helpThreadRunning = false;
                } finally {
                    lock.unlock();
                }
                System.out.println("Ta,"+name+" interrupted helping student " + student.getName());
            }
        });

        lock.lock();
        try {
            helping = true;
            helpThreadRunning = true;
        } finally {
            lock.unlock();
        }

        helpThread.start();
        
    }

    public synchronized boolean isOfficeEmpty() {
        return roomEmpty;
    }

    public synchronized boolean isNapThreadRunning() {
        return napThreadRunning;
    }

    public void waitForAStudent() {

        System.out.println("Office is empty, Ta," + name + " will nap");
            
        while(isOfficeEmpty()) {
            try {
                if(isNapThreadRunning() == false) {
                    taNaps();
                }
            } finally {
                lock.unlock();
            }
        }

        System.out.println("A student has arrived, Ta,"+name+" will help him");

    }

}
