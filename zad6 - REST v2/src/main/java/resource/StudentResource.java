package resource;

import Database.DataBase;
import model.Student;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;


@Path("students/{id}")
@XmlRootElement(name="students")
public class StudentResource extends Resource {

    private DataBase dataBase = DataBase.getInstance();


    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getStudent(@PathParam("id") String id) {
        Response.ResponseBuilder builder = Response.status(200);
        int studentId;
        try {
            studentId = convertId(id);
        } catch (NumberFormatException e) {
            return builder.status(400).build();
        }
        Student student = dataBase.getStudents().get(studentId);

        if (student == null)
            builder.status(Response.Status.NOT_FOUND);
        else builder.entity(student);

        return builder.build();
    }


    @PUT
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response putStudent(Student entity, @PathParam("id") String id) {
        Response.ResponseBuilder builder = Response.status(404);
        int studentId;
        try {
            studentId = convertId(id);
        } catch (NumberFormatException e) {
            return Response.status(400).build();
        }
        Student student = dataBase.getStudents().get(studentId);
        if (student != null) {
            builder.status(304);
            if (entity.getName() != null) {
                student.setName(entity.getName());
                builder.status(200);
            }
            if (entity.getSurname() != null) {
                student.setSurname(entity.getSurname());
                builder.status(200);
            }
            if (entity.getBirthDate() != null) {
                student.setBirthDate(entity.getBirthDate());
                builder.status(200);
            }
        }
        return builder.entity(student).build();
    }

    @DELETE
    public Response deleteStudent(@PathParam("id") String id) {
        Response.ResponseBuilder builder = Response.status(404);
        int studentId;
        try {
            studentId = convertId(id);
        } catch (NumberFormatException e) {
            return Response.status(400).build();
        }
        if (dataBase.getStudents().containsKey(studentId)) {
            builder.status(200);
            dataBase.getStudents().remove(studentId);
        }
        return builder.build();
    }


}