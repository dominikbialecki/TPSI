import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Subject {

    Subject(){}
    Subject(String name, String professor){
        this.name = name;
        this.professor = professor;
    }

    private String name;
    private String professor;

    //getters
    public String getName() {
        return name;
    }
    public String getProfessor() {
        return professor;
    }

    //setters

    public void setName(String name) {
        this.name = name;
    }
    public void setProfessor(String professor) {
        this.professor = professor;
    }

}

