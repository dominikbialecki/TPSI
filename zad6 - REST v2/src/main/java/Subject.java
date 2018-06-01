import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Subject {

    Subject(){}
    Subject(String name, String professor){
        this.id = generateId();
        this.name = name;
        this.professor = professor;
    }

    private static int lastId = 0;
    private synchronized int generateId(){ return lastId++; }

    @XmlElement
    private String name;
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @XmlElement
    private String professor;
    public String getProfessor() { return professor; }
    public void setProfessor(String professor) { this.professor = professor; }

    @XmlAttribute
    private  int id;
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

}

