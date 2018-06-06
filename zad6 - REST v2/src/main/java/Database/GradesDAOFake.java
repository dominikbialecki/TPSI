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
        return this.studentDAO.getData(studentId).getGrades();
    }

    public Grade getData(int gradeId) {
        return this.studentDAO.getGrade(this.studentId, gradeId);
    }

    public void updateData(Grade object) {
        Grade grade = this.studentDAO.getGrade(this.studentId, object.getId());
        grade.setDate(object.getDate());
        grade.setSubject(object.getSubject());
        grade.setValue(object.getValue());
        grade.setStudent(object.getStudent());
    }

    public void deleteData(int gradeId) {
        this.getCollection().remove(this.getData(gradeId));
    }

    public void addData(Grade object) {
        this.getCollection().add(object);
    }

    public boolean containsData(int id) {
        return getCollection().contains(id);
    }
}
