import java.util.HashMap;
import java.util.Map;

public class DataBase {
    private volatile static DataBase instance;

    private DataBase() {
    }

    public static DataBase getInstance() {
        if (instance == null) {
            synchronized (DataBase.class) {
                if (instance == null) {
                    instance = new DataBase();
                }
            }
        }
        return instance;
    }

    public Map<Integer, Student> students = new HashMap<>();
    public Map<Integer, Grade> grades = new HashMap<>();
    public Map<Integer, Subject> subjects = new HashMap<>();

    public void addStudent(Student student){
            students.put(student.getIndex(), student);
    }
//    public void addGrade(Grade grade){
//        this.grades.put(grade.getIndex(), grade);
//    }
    public void addSubject(Subject subject){
        this.subjects.put(subject.getId(), subject);
    }
}