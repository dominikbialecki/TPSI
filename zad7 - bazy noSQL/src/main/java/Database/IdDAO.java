package Database;

import com.mongodb.Mongo;
import model.LastId;
import org.mongodb.morphia.Datastore;
import sun.misc.GC;

public class IdDAO {

    private volatile static IdDAO instance;
    private volatile static Datastore datastore;

    private IdDAO() {
        datastore = MongoDAO.getInstance().getDatastore();
    }

    public static IdDAO getInstance() {
        if (instance == null) {
            synchronized (IdDAO.class) {
                if (instance == null) {
                    instance = new IdDAO();
                }
            }
        }
        return instance;
    }

    Integer getNewGradeId() {
        return getAndIncrement("gradeId").getGradeId();
    }

    Integer getNewStudentIndex() {
        return getAndIncrement("studentIndex").getStudentIndex();
    }

    private LastId getAndIncrement(String field) {
        if (datastore.createQuery(LastId.class).asList().isEmpty()) {
            datastore.save(new LastId(0, 0));
            return new LastId(0, 0);
        }
        return datastore.findAndModify(
                    datastore.find(LastId.class).limit(1),
                    datastore.createUpdateOperations(LastId.class).inc(field)
        );
    }
}
