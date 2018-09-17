package Database;

import model.Subject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SubjectDAOFake implements DAOInterface<Subject>{

    private volatile static SubjectDAOFake instance;
    private static Map<Integer, Subject> subjects;

    private SubjectDAOFake() {
        Subject subject1 = new Subject("Analiza Matematyczna", "Stefan Banach");
        Subject subject2 = new Subject("Algebra Liniowa", "Małgorzata Zygora");
        Subject subject3 = new Subject("Przyroda", "Charles Darwin");
        subjects = new HashMap<Integer, Subject>(){
            {
                put(subject1.getId(), subject1);
                put(subject2.getId(), subject2);
                put(subject3.getId(), subject3);
            }
        };
    }

    public static SubjectDAOFake getInstance() {
        if (instance == null) {
            synchronized (SubjectDAOFake.class) {
                if (instance == null) {
                    instance = new SubjectDAOFake();
                }
            }
        }
        return instance;
    }




    @Override
    public Collection<Subject> getCollection() {
        return subjects.values();
    }

    @Override
    public Subject getData(int id) {
        return subjects.get(id);
    }

    @Override
    public void updateData(Subject object) {
        subjects.put(object.getId(), object);
    }

    @Override
    public void deleteData(int id) {
        GradesDAOFake.removeGradesWithSubjectId(id);
        subjects.remove(id);
    }

    @Override
    public Subject addData(Subject object) {
        Subject subject = new Subject(
                object.getName(),
                object.getProfessor()
        );
        subjects.put(subject.getId(), subject);
        return subject;
    }

    @Override
    public boolean containsData(int id) {
        return subjects.containsKey(id);
    }
}
