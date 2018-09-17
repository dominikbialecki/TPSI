package resource;
import model.Subject;
import service.SubjectService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;


@Path("subjects")
@XmlRootElement(name="subjects")
public class SubjectResource {
    private SubjectService subjectService = new SubjectService();



    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Collection<Subject> getSubjects() {
        return this.subjectService.getSubjects();
    }


    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getSubject(@PathParam("id") String id) {
        return this.subjectService.getSubject(id);
    }

    @PUT
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response putSubject(Subject entity, @PathParam("id") String id) {
        return this.subjectService.putSubject(entity, id);
    }

    @POST
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response postSubject(Subject entity){
        String path = "subjects/";
        return this.subjectService.postSubject(entity, path);
    }

    @DELETE
    @Path("{id}")
    public Response deleteSubject(@PathParam("id") String id){
        return this.subjectService.deleteSubject(id);

    }

}


