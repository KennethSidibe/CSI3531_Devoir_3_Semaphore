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
        return false;
    }

    public void taNaps() {

    }

    public void wakeTaUp() {

    }


    
}
