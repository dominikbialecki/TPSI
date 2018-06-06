package Database;

import model.Student;

import java.util.Collection;

public interface DAOInterface<T> {


    Collection<T> getCollection();
    T getData(int id);
    void updateData(T object);
    void deleteData(int id);
    void addData(T object);
    boolean containsData(int id);
}
