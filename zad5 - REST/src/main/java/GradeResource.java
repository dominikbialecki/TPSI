import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;


@Path("students/{id}/grades")
@XmlRootElement(name="grades")
public class GradeResource {

    DataBase dataBase = DataBase.getInstance();
    @XmlElement(name="grade")
    Collection<Grade> grades;

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Response getAll(@PathParam("id") String id) {
        Student student = dataBase.students.get(Integer.parseInt(id));
        grades = student.getGrades();
        return Response.status(200).entity(this).build();
    }

    @GET
    @Path("{gradeId}")
    @Produces(MediaType.APPLICATION_XML)
    public Response getSubject(@PathParam("id") String id, @PathParam("gradeId") String gradeId) {
        Student student = dataBase.students.get(Integer.parseInt(id));
        Grade grade = student.getGrades().get(Integer.parseInt(gradeId));
        return Response.status(200).entity(grade).build();
    }

}




