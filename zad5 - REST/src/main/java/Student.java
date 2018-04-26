import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Date;

@XmlRootElement
public class Student {

    Student(){}
    Student(int index, String name, String surname, Date birthDate){
        this.index = index;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
    }

    private int index;
    private String name;
    private String surname;
    private Date birthDate;
    private ArrayList<Grade> grades;

    //getters
    public int getIndex() {
        return index;
    }
    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }
    public Date getBirthDate(){
        return birthDate;
    }
    public ArrayList<Grade> getGrades() {
        return grades;
    }


    //setters
    public void setIndex(int index) {
        this.index = index;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public void setBirthDate(Date birthDate){
        this.birthDate = birthDate;

    }
    public void setGrades(ArrayList<Grade> grades) {
        this.grades = grades;
    }

}