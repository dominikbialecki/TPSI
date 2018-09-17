package Database;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

public class MongoDAO {

    private volatile static MongoDAO instance;
    private Datastore datastore;

    private MongoDAO() {
        MongoClient mongo = new MongoClient("localhost", 8004);
        Morphia morphia = new Morphia();
        datastore = morphia.createDatastore(mongo, "university");
        morphia.mapPackage("model");
    }

    public static MongoDAO getInstance() {
        if (instance == null) {
            synchronized (MongoDAO.class) {
                if (instance == null) {
                    instance = new MongoDAO();
                }
            }
        }
        return instance;
    }

    protected Datastore getDatastore() {
        return datastore;
    }
}
