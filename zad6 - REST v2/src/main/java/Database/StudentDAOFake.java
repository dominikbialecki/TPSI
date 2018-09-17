package Database;

import model.Grade;
import model.Student;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class StudentDAOFake implements DAOInterface<Student> {

    private static Map<Integer, Student> students;
    private static volatile StudentDAOFake instance;

    private StudentDAOFake(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Student student1 = new Student("Dominik", "Bialecki", dateFormat.parse("03-09-1995"));
            Student student2 = new Student("Irena", "Nowak", dateFormat.parse("26-11-2006"));
            Student student3 =  new Student("Jan", "Kowalski", dateFormat.parse("14-06-1992"));
            SubjectDAOFake subjectDAO = SubjectDAOFake.getInstance();
            Grade grade1 = new Grade(5f, new Date(), subjectDAO.getData(0));
            Grade grade2 = new Grade(3.5f, new Date(), subjectDAO.getData(1));

            student2.setGrades(new HashMap<Integer, Grade>(){
                {
                    put(grade1.getId(), grade1);
                    put(grade2.getId(), grade2);
                }
            });
            students = new HashMap<Integer, Student>(){
                {
                    put(student1.getIndex(), student1);
                    put(student2.getIndex(), student2);
                    put(student3.getIndex(), student3);
                }
            };
        } catch (ParseException e) {
            System.out.println("Data parsing exception");
            e.printStackTrace();
        }
    }

    public static StudentDAOFake getInstance() {
        if (instance == null) {
            synchronized (StudentDAOFake.class) {
                if (instance == null) {
                    instance = new StudentDAOFake();
                }
            }
        }
        return instance;
    }

    @Override
    public Collection<Student> getCollection() {
        return students.values();
    }


    @Override
    public Student getData(int id) {
        return students.get(id);
    }

    @Override
    public void updateData(Student student) {
        students.put(student.getIndex(), student);
    }

    @Override
    public void deleteData(int id) {
        students.remove(id);
    }

    @Override
    public Student addData(Student entity) {
        Student student = new Student(
                entity.getName(),
                entity.getSurname(),
                entity.getBirthDate());
        students.put(student.getIndex(), student);
        return student;
    }

    @Override
    public boolean containsData(int id) {
        return students.containsKey(id);
    }
}