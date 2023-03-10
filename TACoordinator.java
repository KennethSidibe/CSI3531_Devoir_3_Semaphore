import java.util.concurrent.locks.ReentrantLock;

// This class is responsible for processing all the 
// logic necessary so that the TA 
// can focus on helping Student and not the 
// logistic part 
// See Devoir3 directives for specifications

// Kenneth Sidibe
// 300099184

public class TACoordinator implements Runnable {

    private ReentrantLock lock = new ReentrantLock();
    private TeacherAssistant tAssistant = new TeacherAssistant();
    
    private boolean waitRoomFull = false;
    private boolean waitRoomEmpty = true;
    
    private int waitCursor = -1;
    private Student[] waitRoom = new Student[waitChairs];

    private Student[] studentsToHelp = new Student[maxServerCapacity];
    private int studentHelpIndex = 0;
    private int lastStudentHelpCursor = 0;
    
    private boolean napping = false;
    private boolean helping = false;
    private boolean studentWaiting = false;
    private boolean noStudentToHelp = false;

    private boolean manageQueueThreadRunnig = false;
    private boolean manageTaThreadRunnig = false;
    private boolean manageStudentsThreadRunnig = false;
    private boolean fillWaitRoomThreadRunning = false;

    private Thread napThread;
    private Thread helpingThread;
    private Thread fillThread;
    private Thread dequeueThread;

    private Thread taCoordinatorThread;
    
    private static final int waitChairs = 3;
    private static final int maxServerCapacity = 100;
    
    TACoordinator(TeacherAssistant tAssistant, Student[] studentsToHelpInitList) {

        this.tAssistant = tAssistant;
        this.lastStudentHelpCursor = studentsToHelpInitList.length-1;
        this.taCoordinatorThread = new Thread(this);

        if(studentsToHelpInitList.length == maxServerCapacity) {
            this.studentsToHelp = studentsToHelpInitList;
        }

        else {
            for (int i = 0; i < studentsToHelpInitList.length; i++) {
                this.studentsToHelp[i] = studentsToHelpInitList[i];
            }
        }

    }

    public void enqueue(Student newStudent) {

        
        if(waitRoomFull) {
            System.out.println("Wait office is full, Student needs to come back later");
        }
        
        else {

            waitCursor++;
            waitRoom[waitCursor] = newStudent;
            
            if(waitCursor >= waitRoom.length - 1) {
                waitRoomFull = true;
            }

        }
        
    }

    private void fillThreadMethod() {

        while( !(isWaitRoomFull()) || areStudentsToHelpRemaining() ) {

            if(Thread.interrupted()) {
                System.out.println("thread interrupted\n");
                break;
            }

            lock.lock();
            try {
                if(isFillWaitRoomThreadRunning() == false){

                    lock.lock();
                    try {
                        fillWaitRoomThreadRunning = true;
                    } finally {
                        lock.unlock();
                    }

                    fillWaitRoom();
                }
            } finally {
                lock.unlock();
            }

        }

        System.out.println("students filled finished");
        
    }

    public void manageQueue() {
        
    }

    public void removeStudentFromStudentsToHelp() {
        if(studentHelpIndex > lastStudentHelpCursor) {
            return;
        }

        studentsToHelp[studentHelpIndex] = null;
        studentHelpIndex++;
    }

    public Student dequeue() {

        waitRoomFull = false;

        // 1 student in wait room
        if(waitCursor == 0) {
            
            Student studentFinishWait = waitRoom[0];
            waitRoom[0] = null;
            waitCursor--;

            return studentFinishWait;
        }

        // 2 student in wait room
        else if (waitCursor == 1) {
            Student studentFinishWait = waitRoom[0];
            waitRoom[0] = null;

            // placing remaining student in queue
            waitRoom[0] = waitRoom[1];
            waitRoom[1] = null;

            waitCursor--;

            return studentFinishWait;
        }
        
        // More than 2 student
        else if (waitCursor > 1) {

            Student studentFinishWait = waitRoom[0];

            for(int i = waitCursor; i > 1; i --) {
                
                waitRoom[0] = null;

                Student tempStudent = waitRoom[i-1];
                waitRoom[i-1] = waitRoom[i];
                waitRoom[i] = null;
                waitRoom[i-2] = tempStudent;

                waitCursor--;
            }

            return studentFinishWait;
        }

        // No student in queue
        else {
            System.out.println("Wait office is empty");
            waitRoomEmpty = true;
            return new Student(null);
        }

    }

    public void wakeTaUp() {

        try {
            if(!Thread.interrupted()) {
                napThread.interrupt();
            }
            else {
                System.out.println("Ta is already awake !");
            }
        } catch(Exception e) {
            System.out.println("no thread is running");
        }
        
    }

    private void fillWaitRoom() {
        
        for (int i = studentHelpIndex; i < studentsToHelp.length; i++) {

            if(isWaitRoomFull()) {
                break;
            }

            enqueue(studentsToHelp[studentHelpIndex]);
            removeStudentFromStudentsToHelp();
            
        }

        lock.lock();
        try {
            fillWaitRoomThreadRunning = false;
        } finally {
            lock.unlock();
        }
    }

    private void passNextStudentToTa() {

    } 

    private synchronized boolean isFillWaitRoomThreadRunning() {
        return this.fillWaitRoomThreadRunning;
    }
    
    
    public synchronized boolean isTaNapping() {
        return napping;
    }

    public synchronized boolean isTaHelpingStudent() {
        return helping;
    }

    public synchronized boolean isStudentWaiting() {
        return studentWaiting;
    }

    public synchronized void taIsNowAvailable() {
        this.helping = false;
    }

    public synchronized void taIsNotAvailableAnymore() {
        this.helping = true;
    }
    
    public synchronized boolean isWaitRoomFull() {
        
        waitRoomFull = waitRoom[waitRoom.length - 1] != null;
        return waitRoom[waitRoom.length-1] != null;

    }
    
    public synchronized boolean isWaitRoomEmpty() {

        for (int i = 0; i < waitRoom.length; i++) {
            if(waitRoom[i] != null) {
                waitRoomEmpty = false;
                return false;
            }
        }

        waitRoomEmpty = true;
        return true;

    }

    public Student[] getStudents() {
        return this.studentsToHelp;
    }

    public String[] getAllStudentsToHelpName() {
        String[] nameList = new String[this.lastStudentHelpCursor+1];

        for (int i = 0; i < nameList.length; i++) {
            nameList[i] = studentsToHelp[i].getName();
        }

        return nameList;

    }

    public String[] getAllStudentsInWaitRoomName() {

        String[] nameList = new String[waitRoom.length];

        for (int i = 0; i < nameList.length; i++) {
            Student student = waitRoom[i];
            if(student == null) {
                break;
            }
            nameList[i] = student.getName();
        }

        return nameList;
    }

    public void interruptFillThread() {
        this.fillThread.interrupt();
    }

    public void interruptTaCoordinatorThread() {
        this.taCoordinatorThread.interrupt();
    }

    public synchronized boolean areStudentsToHelpRemaining() {
        return studentsToHelp[studentHelpIndex] != null;
    }

    @Override
    public void run() {

        fillThread = new Thread(() -> fillThreadMethod());
        fillThread.start();

    }

    public void start() {
        
        this.taCoordinatorThread.start();

    }

    // v2 feature
    public void appendNewStudentsToHelp(Student[] newStudentsToHelp) {

        int newStudentCursor = 0;

        if(newStudentsToHelp.length == maxServerCapacity) {
            System.out.println("new student list is too large");
            return;
        }

        for (int i = lastStudentHelpCursor; i < studentsToHelp.length && newStudentCursor < newStudentsToHelp.length; i++) {
            this.studentsToHelp[i] = newStudentsToHelp[newStudentCursor];
            newStudentCursor++;
        }
        
    }

}
