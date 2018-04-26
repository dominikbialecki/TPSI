import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
public class Grade {

    Grade(){}
    Grade(float value, Date date, Subject subject){
        this.value = value;
        this.date = date;
        this.subject = subject;
    }

    private float value;
    private Date date;
    private Subject subject;


    //getters
    public float getValue() {
        return value;
    }
    public Date getDate() {
        return date;
    }
    public Subject getSubject() {
        return subject;
    }

    //setters
    public void setValue(float value) {
        this.value = value;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public void setSubject(Subject subject) {
        this.subject = subject;
    }

}
