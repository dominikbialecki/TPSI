import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;


@Path("students")
@XmlRootElement(name="students")
public class StudentResource {

    DataBase dataBase = DataBase.getInstance();
    @XmlElement(name="students")
    Collection<Student> students;

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Response getAll() {
        students = dataBase.students.values();
        return Response.status(200).entity(this).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_XML)
    public Response getSubject(@PathParam("id") String id) {
        System.out.println("found subject with id: "+id);
        System.out.println(dataBase.students.get(Integer.parseInt(id)).getName());
        Student student = dataBase.students.get(Integer.parseInt(id));
        return Response.status(200).entity(student).build();
    }

}




