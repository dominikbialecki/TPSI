package Database;

import model.Grade;
import model.Student;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

import javax.ws.rs.NotFoundException;
import java.util.*;

public class GradesDAO {

    private StudentDAO studentDAO = StudentDAO.getInstance();
    private IdDAO idDAO = IdDAO.getInstance();
    private volatile static Datastore datastore;
    private volatile static GradesDAO instance;

    private GradesDAO() {}

    public static GradesDAO getInstance() {
        if (instance == null) {
            synchronized (GradesDAO.class) {
                if (instance == null) {
                    instance = new GradesDAO();
                    datastore = MongoDAO.getInstance().getDatastore();
                }
            }
        }
        return instance;
    }

    private Student getStudent(int studentIndex) {
        return studentDAO.getData(studentIndex);
    }

    public Collection<Grade> getCollection(int studentIndex, ObjectId subjectIdParam, Float minValueParam, Float maxValueParam) {

        Optional<ObjectId> subjectId = Optional.ofNullable(subjectIdParam);
        Optional<Float> minValue = Optional.ofNullable(minValueParam);
        Optional<Float> maxValue = Optional.ofNullable(maxValueParam);

        Collection<Grade> grades = getStudent(studentIndex).getGrades().values();

        subjectId.ifPresent(id -> grades.removeIf(grade -> !grade.getSubject().getId().equals(id)));
        minValue.ifPresent(f -> grades.removeIf(grade -> grade.getValue() < f));
        maxValue.ifPresent(f -> grades.removeIf(grade -> grade.getValue() > f));

        return grades;
    }

    public Grade getData(int studentIndex, int gradeId) {
        Grade data = this.getStudent(studentIndex).getGrades().values().stream()
                .filter(grade -> grade.getId() == gradeId)
                .findFirst().orElse(null);
        if (data == null) throw new NotFoundException("Grade does not exist");
        return data;

    }

    public Grade addData(int studentIndex, Grade object) {
        Student student = getStudent(studentIndex);
        int gradeId = idDAO.getNewGradeId();
        object.setId(gradeId);
        student.getGrades().put(gradeId, object);
        studentDAO.updateData(student);
        return object;
    }

    public void updateData(int studentIndex, Grade object) {
        Student student = getStudent(studentIndex);
        int gradeId = idDAO.getNewGradeId();
        object.setId(gradeId);
        student.getGrades().put(gradeId, object);
        datastore.save(student);
    }

    public void deleteData(int studentIndex, int gradeId) {
        Student student = getStudent(studentIndex);
        student.getGrades().remove(gradeId);
        datastore.save(student);
    }

    public boolean containsData(int studentIndex, int gradeId) {
        return getStudent(studentIndex).getGrades().containsKey(gradeId);
    }
}
