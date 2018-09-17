package Database;

import model.Student;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class StudentDAO {

    private IdDAO idDAO = IdDAO.getInstance();
    private static volatile StudentDAO instance;
    private volatile static Datastore datastore;

    private StudentDAO() {
    }

    public static StudentDAO getInstance() {
        if (instance == null) {
            synchronized (StudentDAO.class) {
                if (instance == null) {
                    instance = new StudentDAO();
                    datastore = MongoDAO.getInstance().getDatastore();
                }
            }
        }
        return instance;
    }

    public Collection<Student> getCollection(String nameParam,
                                             String surnameParam,
                                             Date dateFromParam,
                                             Date dateToParam) {
        Query<Student> query = datastore.createQuery(Student.class);

        Optional<String> name = Optional.ofNullable(nameParam);
        Optional<String> surname = Optional.ofNullable(surnameParam);
        Optional<Date> dateFrom = Optional.ofNullable(dateFromParam);
        Optional<Date> dateTo = Optional.ofNullable(dateToParam);

        name.ifPresent(s -> query.and(query.criteria("name").equal(s)));
        surname.ifPresent(s -> query.and(query.criteria("surname").equal(s)));
        dateFrom.ifPresent(date -> query.and(query.criteria("birthDate").greaterThanOrEq(date)));
        dateTo.ifPresent(date -> query.and(query.criteria("birthDate").lessThanOrEq(date)));

        return query.asList();
    }

    public Student getData(int id) {
        return datastore.find(Student.class, "index", id).get();
    }

    public Student addData(Student object) {
        object.setIndex(idDAO.getNewStudentIndex());
        datastore.save(object);
        return object;
    }

    public void updateData(Student student) {
        Student current = datastore.find(Student.class, "index", student.getIndex()).get();
        student.setIndex(current.getIndex());
        student.setId(current.getId());
        datastore.save(student);
    }

    public void deleteData(int id) {
        datastore.delete(datastore.find(Student.class, "index", id));
    }

    public boolean containsData(int id) {
        return !datastore.find(Student.class, "index", id).asList().isEmpty();
    }
}