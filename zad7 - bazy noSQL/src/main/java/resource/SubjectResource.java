package resource;
import model.Subject;
import org.bson.types.ObjectId;
import service.SubjectService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;


@Path("subjects")
@XmlRootElement(name="subjects")
public class SubjectResource {
    private SubjectService subjectService = new SubjectService();



    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Collection<Subject> getSubjects(@QueryParam("professor") String professor) {
        return this.subjectService.getSubjects(professor);
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getSubject(@PathParam("id") ObjectId id) {
        return this.subjectService.getSubject(id);
    }

    @PUT
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response putSubject(Subject entity, @PathParam("id") ObjectId id) {
        return this.subjectService.putSubject(entity, id);
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response postSubject(Subject entity){
        String path = "subjects/";
        return this.subjectService.postSubject(entity, path);
    }

    @DELETE
    @Path("{id}")
    public Response deleteSubject(@PathParam("id") ObjectId id){
        return this.subjectService.deleteSubject(id);

    }

}


