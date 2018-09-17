package resource;

import model.Grade;
import org.bson.types.ObjectId;
import service.GradesService;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;


@Path("students/{studentId}/grades")
@XmlRootElement(name="grades")
public class GradeResource {

    private GradesService gradesService = new GradesService();


    @XmlElement(name="grade")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Collection<Grade> getGrades(@Context UriInfo info,
                                       @PathParam("studentId") int studentId) {
        String subjectIdString = info.getQueryParameters().getFirst("subjectId");
        String minValue = info.getQueryParameters().getFirst("minValue");
        String maxValue = info.getQueryParameters().getFirst("maxValue");
        ObjectId subjectId = (subjectIdString != null) ? new ObjectId(subjectIdString) : null;
        return gradesService.getGrades(studentId, subjectId, minValue, maxValue);
    }

    @GET
    @Path("{gradeId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getGrade(@PathParam("studentId") int studentId, @PathParam("gradeId") int gradeId) {
        return gradesService.getGrade(studentId, gradeId);
    }

    @PUT
    @Path("{gradeId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response putGrade(Grade entity, @PathParam("studentId") int studentId, @PathParam("gradeId") int gradeId){
        return gradesService.putGrade(studentId, entity, gradeId);
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response postGrade(Grade entity, @PathParam("studentId") int studentId){
//        URI path = new URI("students/"+studentId+"grades/");
        String path = "students/"+studentId+"/grades/";
        return gradesService.postGrade(studentId, entity, path);
    }

    @DELETE
    @Path("{gradeId}")
    public Response deleteGrade(@PathParam("studentId") int studentId, @PathParam("gradeId") int gradeId){
        return gradesService.deleteGrade(studentId, gradeId);
    }
}




