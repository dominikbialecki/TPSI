package resource;

import Database.DataBase;
import model.Subject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.List;


@Path("subjects")
@XmlRootElement(name="subjects")
public class SubjectResource extends Resource {
    private DataBase dataBase = DataBase.getInstance();



    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Collection<Subject> getSubjects() {
        Collection<Subject> subjects = dataBase.getSubjects().values();
        return subjects;
    }


    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getSubject(@PathParam("id") int id) {
        Subject subject = dataBase.getSubjects().get(id);
        if (subject == null){
            return Response.status(404).build();
        }
        return Response.status(200).entity(subject).build();
    }

    @PUT
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response putSubject(Subject entity, @PathParam("id") int id) {
        Subject subject = dataBase.getSubjects().get(id);
        if (subject == null){
            return Response.status(404).build();
        }
        boolean modified = false;
        if (entity.getName()!=null){
            subject.setName(entity.getName());
            modified = true;
        }
        if (entity.getProfessor()!=null){
            subject.setProfessor(entity.getProfessor());
            modified = true;
        }
        if (modified)
            return Response.status(200).entity(entity).build();
        else
            return Response.status(304).entity(entity).build();
    }

    @POST
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response postSubject(Subject entity){
        if (dataBase.getSubjects().containsKey(entity.getId()))
            return Response.status(403).build();
        if (entity.getProfessor()!=null & entity.getName()!=null){
            dataBase.addSubject(entity);
            return Response.status(201).entity(entity).build();
        }
        return Response.status(400).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteSubject(@PathParam("id") String id){
        Response.ResponseBuilder builder = Response.status(404);
        int subjectId;
        try {
            subjectId = convertId(id);
        } catch (NumberFormatException e) {
            return Response.status(400).build();
        }
        if (dataBase.getSubjects().containsKey(subjectId)) {
            builder.status(200);
            dataBase.getSubjects().remove(subjectId);
        }
        return builder.build();

    }

}


