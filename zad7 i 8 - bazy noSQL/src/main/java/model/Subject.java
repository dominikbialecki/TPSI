package model;

import org.bson.types.ObjectId;
import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import resource.SubjectResource;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Subject {

    Subject() {
    }

    public Subject(ObjectId id, String name, String professor) {
        this.id = id;
        this.name = name;
        this.professor = professor;
    }

    @XmlJavaTypeAdapter(ObjectIdJaxbAdapter.class)
    @XmlAttribute
    @Id
    private ObjectId id;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    @XmlElement
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    private String professor;

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

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
    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    List<Link> links;
}

