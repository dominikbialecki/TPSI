package resource;

import model.Grade;
import service.GradesService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;


@Path("students/{studentId}/grades")
@XmlRootElement(name="grades")
public class GradeResource {


    @XmlElement(name="grade")

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Collection<Grade> getGrades(@PathParam("studentId") String studentId) {
        return new GradesService(studentId).getGrades();
    }

    @GET
    @Path("{gradeId}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getGrade(@PathParam("studentId") String studentId, @PathParam("gradeId") String gradeId) {
        return new GradesService(studentId).getGrade(gradeId);
    }

    @PUT
    @Path("{gradeId}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response putGrade(Grade entity, @PathParam("studentId") String studentId, @PathParam("gradeId") String gradeId){
        return new GradesService(studentId).putGrade(entity, gradeId);
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response postGrade(Grade entity, @PathParam("studentId") String studentId){
        return new GradesService(studentId).postGrade(entity);
    }

    @DELETE
    @Path("{gradeId}")
    public Response deleteGrade(@PathParam("studentId") String studentId, @PathParam("gradeId") String gradeId){
        return new GradesService(studentId).deleteGrade(gradeId);
    }
}




