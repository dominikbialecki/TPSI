package Database;

import model.Grade;
import model.Student;

import javax.ws.rs.NotFoundException;
import java.util.Collection;
import java.util.stream.Collectors;

public class GradesDAOFake implements DAOInterface<Grade> {

    private int studentId;
    public GradesDAOFake(int studentId) throws NotFoundException{
        if (studentDAO.containsData(studentId))
            this.studentId = studentId;
        else throw new NotFoundException("student does not exist");
    }


    private StudentDAOFake studentDAO = StudentDAOFake.getInstance();

    public Collection<Grade> getCollection() {
        return this.studentDAO.getData(studentId).getGrades().values();
    }

    public Grade getData(int gradeId) {
        return this.studentDAO.getData(studentId).getGrades().get(gradeId);
    }

    public void updateData(Grade object) {
        Grade grade = this.getData(object.getId());
        grade.setDate(object.getDate());
        grade.setSubject(object.getSubject());
        grade.setValue(object.getValue());
        this.studentDAO.getData(studentId).getGrades().put(grade.getId(), grade);
    }

    public void deleteData(int gradeId) {
        this.studentDAO.getData(studentId).getGrades().remove(gradeId);
    }

    private static void deleteData(int studentId, int gradeId) {
        StudentDAOFake.getInstance().getData(studentId).getGrades().remove(gradeId);
    }

    public Grade addData(Grade object) {
        Grade grade = new Grade(
                object.getValue(),
                object.getDate(),
                object.getSubject()
        );
        this.studentDAO.getData(studentId).getGrades().put(grade.getId(), grade);
        return grade;
    }

    public boolean containsData(int id) {
        return studentDAO.getData(studentId).getGrades().containsKey(id);
    }

    static public void removeGradesWithSubjectId(int subjectID){
        StudentDAOFake studentDAO = StudentDAOFake.getInstance();
        studentDAO.getCollection().forEach(student ->
        student.getGrades().values().stream()
            .filter(grade -> grade.getId() == subjectID)
            .collect(Collectors.toList())
            .forEach(grade -> new GradesDAOFake(student.getIndex()).deleteData(grade.getId()))
        );
    }
}
