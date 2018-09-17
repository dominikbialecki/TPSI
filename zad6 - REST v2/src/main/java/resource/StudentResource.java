package resource;

import model.Student;
import service.StudentService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;


@Path("students")
@XmlRootElement(name="students")
public class StudentResource {

    private StudentService studentService = new StudentService();


    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Collection<Student> getStudents() {
        return this.studentService.getStudents();
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getStudent(@PathParam("id") String id) {
        return this.studentService.getStudent(id);
    }


    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response postSubject(Student entity) {
        String path = "students/";
        return this.studentService.postStudent(entity, path);
    }

    @Path("{id}")
    @PUT
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response putStudent(Student entity, @PathParam("id") String id) {
        return this.studentService.putStudent(entity, id);
    }

    @Path("{id}")
    @DELETE
    public Response deleteStudent(@PathParam("id") String id) {
        return this.studentService.deleteStudent(id);
    }

}