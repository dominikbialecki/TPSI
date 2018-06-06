package Database;

import model.Grade;

import javax.ws.rs.NotFoundException;
import java.util.Collection;

public class GradesDAOFake implements DAOInterface<Grade> {

    private int studentId;
    public GradesDAOFake(int studentId) throws NotFoundException{
        if (studentDAO.containsData(studentId))
            this.studentId = studentId;
        else throw new NotFoundException("student does not exists");
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

    public void addData(Grade object) {
        this.getCollection().add(object);
    }

    public boolean containsData(int id) {
        return studentDAO.getData(studentId).getGrades().containsKey(id);
    }
}
