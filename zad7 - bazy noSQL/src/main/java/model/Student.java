package model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.bson.types.ObjectId;
import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.mongodb.morphia.annotations.*;
import resource.GradeResource;
import resource.StudentResource;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.*;

@Entity
@XmlRootElement(name="student")
@XmlAccessorType(XmlAccessType.NONE)
public class Student {

    private static int lastIndex = 0;
    private synchronized int generateIndex(){
        return lastIndex++;
    }


    Student(){
        this.grades = new HashMap<>();
    }
    public Student(String name, String surname, Date birthDate){
        this.index = generateIndex();
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.grades = new HashMap<>();
    }

    @Id
    @XmlTransient
    private ObjectId id;
    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    @Indexed(options = @IndexOptions(unique = true))
    @XmlAttribute(name="index")
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


    @XmlElement
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd",  timezone="CET")
    private Date birthDate;
    public Date getBirthDate(){
        return birthDate; }
    public void setBirthDate(Date birthDate){ this.birthDate = birthDate; }

    @InjectLinks({
            @InjectLink(
                    rel = "self",
                    resource = StudentResource.class,
                    bindings = @Binding(name = "id", value = "${instance.index}"),
                    method = "getData"),
            @InjectLink(
                    rel = "parent",
                    method = "getCollection",
                    resource = StudentResource.class),
            @InjectLink(
                    rel = "grades",
                    method = "getGrades",
                    resource = GradeResource.class,
                    bindings = @Binding(name = "studentId", value = "${instance.index}"))
    })
    @XmlElement(name="link")
    @XmlElementWrapper(name = "links")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    List<Link> links;

    @Embedded
    @XmlTransient
    private Map<Integer, Grade> grades;
    public Map<Integer, Grade> getGrades() { return grades; }
    public void setGrades(Map<Integer, Grade> grades) { this.grades = grades; }
}