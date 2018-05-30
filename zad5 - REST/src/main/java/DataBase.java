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

    private Map<Integer, Student> students = new HashMap<>();
    private Map<Integer, Subject> subjects = new HashMap<>();
    private Map<Integer, Grade> grades= new HashMap<>();

    public Map<Integer, Student> getStudents(){
        return this.students;
    }

    public void addStudent(Student student){
            students.put(student.getIndex(), student);
    }


    public Map<Integer, Subject> getSubjects(){
        return this.subjects;
    }

    public void addSubject(Subject subject){
        this.subjects.put(subject.getId(), subject);
    }

    public void addGrade(int studentId, Grade grade){
        students.get(studentId).getGrades().add(grade);
        grades.put(grade.getId(),grade);
    }

    public Grade getGrade(int studentId, int gradeId){
    Student student = students.get(studentId);
        if (student != null) {
            Grade grade = student.getGrades().get(gradeId);
            if (grade != null)
                return grade;
        }
        return null;
    }
}