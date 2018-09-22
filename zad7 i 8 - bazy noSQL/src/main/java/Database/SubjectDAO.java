package Database;

import model.Subject;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;

import javax.ws.rs.WebApplicationException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Optional;

public class SubjectDAO {

    private volatile static SubjectDAO instance;
    private volatile static Datastore datastore;

    private SubjectDAO() {
    }

    public static SubjectDAO getInstance() {
        if (instance == null) {
            synchronized (SubjectDAO.class) {
                if (instance == null) {
                    instance = new SubjectDAO();
                    datastore = MongoDAO.getInstance().getDatastore();
                }
            }
        }
        return instance;
    }

    public Collection<Subject> getCollection(String professorParam, String nameParam) {

        Query<Subject> query = datastore.createQuery(Subject.class);

        Optional<String> professor = Optional.ofNullable(professorParam);
        Optional<String> name = Optional.ofNullable(nameParam);
        professor.ifPresent(s -> query.and(query.criteria("professor").containsIgnoreCase(s)));
        name.ifPresent(s -> query.and(query.criteria("name").containsIgnoreCase(s)));

        return query.asList();
    }

    public Subject getData(ObjectId id) {
        return datastore.find(Subject.class, "id", id).get();
    }

    public Subject addData(Subject object) {
        Key<Subject> key = datastore.save(object);
        object.setId((ObjectId) key.getId());
        return object;
    }

    public void updateData(Subject object) {
        Subject current = datastore.find(Subject.class, "id", object.getId()).get();
        object.setId(current.getId());
        datastore.save(object);
    }

    public void deleteData(ObjectId id) {
        datastore.delete(datastore.find(Subject.class, "id", id));
    }

    public boolean containsData(ObjectId id) {
        return !datastore.find(Subject.class, "id", id).asList().isEmpty();
    }
}
