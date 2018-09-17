package model;

import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.glassfish.jersey.server.ParamException;
import resource.GradeResource;
import resource.StudentResource;
import resource.SubjectResource;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Grade {

    private static int lastId = 0;
    synchronized int generateId(){ return lastId++; }

    Grade(){}
    public Grade(float value, Date date, Subject subject){
        this.value = value;
        this.date = date;
        this.subject = subject;
        this.id = generateId();
    }


    @XmlElement
    private Integer id;
    public Integer getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    @XmlElement
    private Float value;
    public Float getValue() { return value; }
    public void setValue(float value) throws BadRequestException {
        if (value % 0.5 != 0 | value <2 | value >5) throw new BadRequestException();
        this.value = value;
    }


    @XmlElement
    private Date date;
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    @XmlElement
    private Subject subject;
    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }


    @InjectLinks({
            @InjectLink(
                    rel = "self",
                    resource = GradeResource.class,
                    bindings = @Binding(name = "gradeId", value = "${instance.id}"),
                    method = "getGrade"),
            @InjectLink(
                    rel = "parent",
                    resource = GradeResource.class,
                    method = "getGrades"),
            @InjectLink(
                    rel = "student",
                    resource = StudentResource.class,
                    method = "getStudent"),
            @InjectLink(
                    rel = "subject",
                    resource = SubjectResource.class,
                    method = "getSubject",
                    bindings = @Binding(name = "id", value = "${instance.subject.id}")),
    })
    @XmlElement(name="link")
    @XmlElementWrapper(name = "links")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    List<Link> links;



}
