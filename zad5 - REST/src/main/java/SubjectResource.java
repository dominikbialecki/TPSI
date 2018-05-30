import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;


@Path("subjects")
@XmlRootElement(name="subjects")
public class SubjectResource {
    DataBase dataBase = DataBase.getInstance();
    @XmlElement(name="subject")
    Collection<Subject> subjects;

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Response getAll() {
        subjects = dataBase.getSubjects().values();
        return Response.status(200).entity(this).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_XML)
    public Response getSubject(@PathParam("id") int id) {
        Subject subject = dataBase.getSubjects().get(id);
        if (subject == null){
            return Response.status(404).build();
        }
        return Response.status(200).entity(subject).build();
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_XML)
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
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_XML)
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
    public Response deleteSubject(@PathParam("id") int id){
        if(!dataBase.getSubjects().containsKey(id))
            return Response.status(404).build();

        dataBase.getSubjects().remove(id);
        return Response.status(200).build();
    }

}


