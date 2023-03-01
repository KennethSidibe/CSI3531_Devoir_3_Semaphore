import java.util.Random;

// This class is responsible for processing all the 
// logic necessary so that the TA 
// can focus on helping Student and not the 
// logistic part 
// See Devoir3 directives for specifications

// Kenneth Sidibe
// 300099184

public class TACoordinator {

    private int waitChairs = 3;
    private boolean waitRoomFull = false;
    private int StudentLineCursor = -1;
    public Student[] line = new Student[this.waitChairs];
    private boolean napping = false;
    private Thread napsThread;

    public boolean enqueue(Student newStudent) {

        if(StudentLineCursor >= line.length - 1 ) {
            waitRoomFull = true;
        }

        if(waitRoomFull) {
            System.out.println("Wait office is full, Student needs to come back later");
            return false;
        }
        else {
            StudentLineCursor++;
            line[StudentLineCursor] = newStudent;
            return true;
        }
    }

    public Student dequeue() {

        if(StudentLineCursor == 0) {
            Student studentFinishWait = line[0];
            line[0] = null;
            StudentLineCursor--;
            return studentFinishWait;
        }

        else if (StudentLineCursor == 1) {
            Student studentFinishWait = line[0];
            line[0] = null;

            line[0] = line[1];
            line[1] = null;
            StudentLineCursor--;
            return studentFinishWait;
        }
       else if (StudentLineCursor > 1) {

            Student studentFinishWait = line[0];

            for(int i = StudentLineCursor; i > 1; i --) {
                
                line[0] = null;

                Student tempStudent = line[i-1];
                line[i-1] = line[i];
                line[i] = null;
                line[i-2] = tempStudent;

                StudentLineCursor--;
            }

            return studentFinishWait;
        }

        else {
            System.out.println("Wait office is empty");
            return new Student();
        }
    }

    public boolean isTaAvailable() {
        return waitRoomFull;
    }

    public void taNaps() {

        int maxSleepTime = 10000;
        int minSleepTime = 5000;
        int sleepTime = new Random().nextInt(maxSleepTime - minSleepTime) + minSleepTime;
        final int sleepIncrementConstant = 1000;

        napsThread = new Thread(() -> {
            try {

                for (int i = 0; i <= sleepTime; i+=sleepIncrementConstant) {
                    System.out.println("Zzz");
                    Thread.sleep(sleepIncrementConstant);
                }

                System.out.println("Ta finishes napping");
                
            }
            catch (InterruptedException e) {
                System.out.println("TA got waked up");
            }
        });

        napsThread.start();

    }

    public void wakeTaUp() {

        if(!Thread.interrupted()) {
            napsThread.interrupt();
        }
        else {
            System.out.println("Ta is already awake !");
        }
        
    }
    
}
