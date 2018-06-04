package model;

import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLinks;
import resource.GradeResource;
import resource.StudentResource;
import resource.SubjectResource;
import org.glassfish.jersey.linking.InjectLink;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.net.URI;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Subject {

    Subject(){}
    public Subject(String name, String professor){
        this.id = generateId();
        this.name = name;
        this.professor = professor;
    }

    private static int lastId = 0;
    private synchronized int generateId(){ return lastId++; }


    @XmlAttribute
    private  int id;
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @XmlElement
    private String name;
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }



    @XmlElement
    private String professor;
    public String getProfessor() { return professor; }
    public void setProfessor(String professor) { this.professor = professor; }


    @InjectLinks({
            @InjectLink(
                    rel = "self",
                    resource = SubjectResource.class,
                    bindings = @Binding(name = "id", value = "${instance.id}"),
                    method = "getSubject"),
            @InjectLink(
                    rel = "parent",
                    resource = SubjectResource.class,
                    method = "getSubjects"),
    })
    @XmlElement(name="link")
    @XmlElementWrapper(name = "links")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    List<Link> links;


}

