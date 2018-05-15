import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Date;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Student {

    private static int lastIndex = 0;
    private synchronized int generateIndex(){
        return lastIndex++;
    }

    Student(){}
    Student( String name, String surname, Date birthDate){
        this.index = generateIndex();
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.grades = new ArrayList<>();
    }

    @XmlElement(name="index")
    private int index;
    public int getIndex() { return index; }
    public void setIndex(int index) { this.index = index; }

    @XmlElement(name="name")
    private String name;
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @XmlElement(name="surname")
    private String surname;
    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    @XmlElement(name="grades")
    private ArrayList<Grade> grades;
    public ArrayList<Grade> getGrades() { return grades; }
    public void setGrades(ArrayList<Grade> grades) { this.grades = grades; }

    @XmlElement
    @XmlSchemaType(name="date")
    private Date birthDate;
    public Date getBirthDate(){ return birthDate; }
    public void setBirthDate(Date birthDate){ this.birthDate = birthDate; }


    //setters



}