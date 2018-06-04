package resource;

import Database.DataBase;
import model.Student;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;


@Path("students")
@XmlRootElement(name="students")
public class StudentsResource extends Resource {


    private DataBase dataBase = DataBase.getInstance();


    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Collection<Student> getStudents() {
        Collection<Student> students = dataBase.getStudents().values();
        return students;
    }


    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response postSubject(Student entity) {
        if (dataBase.getStudents().containsKey(entity.getIndex())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        if (entity.getName() != null && entity.getSurname() != null && entity.getBirthDate() != null) {
            dataBase.addStudent(entity);
            return Response.status(201).entity(entity).build();
        }
        return Response.status(400).build();
    }
}