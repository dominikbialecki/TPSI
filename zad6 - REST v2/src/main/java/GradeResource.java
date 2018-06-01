import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;


@Path("students/{studentId}/grades")
@XmlRootElement(name="grades")
public class GradeResource {


    DataBase dataBase = DataBase.getInstance();
    @XmlElement(name="grade")
    List<Grade> grades;

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Response getAllGrades(@PathParam("studentId") int studentId) {
        Student student = dataBase.getStudents().get(studentId);
        if (student != null) {
            grades = student.getGrades();
            return Response.status(200).entity(this).build();
        }
        return Response.status(404).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllGradesJson(@PathParam("studentId") int studentId) {
        Student student = dataBase.getStudents().get(studentId);
        if (student != null) {
            grades = student.getGrades();
            return Response.status(200).entity(grades).build();
        }
        return Response.status(404).build();
    }

    @GET
    @Path("{gradeId}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getGrade(@PathParam("studentId") int studentId, @PathParam("gradeId") int gradeId) {
        Grade grade = dataBase.getGrade(studentId, gradeId);
        if (grade != null) {
            return Response.status(200).entity(grade).build();
        }
        return Response.status(404).build();
    }

    @PUT
    @Path("{gradeId}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response putGrade(Grade entity, @PathParam("studentId") int studentId, @PathParam("gradeId") int gradeId){
        Grade grade = dataBase.getGrade(studentId, gradeId);
        if (grade == null){
            return Response.status(404).build();
        }
        boolean modified = false;
        if (entity.getDate()!=null){
            grade.setDate(entity.getDate());
            modified = true;
        }
        if (entity.getSubject()!=null){
            grade.setSubject(entity.getSubject());
            modified = true;
        }
        if (entity.getValue()!=null){
            grade.setValue(entity.getValue());
            modified = true;
        }
        if (modified)
            return Response.status(200).entity(entity).build();
        return Response.status(304).build();
    }

    @POST
    @Path("{gradeId}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response postGrade(Grade entity, @PathParam("studentId") int studentId){

        if (!dataBase.getSubjects().containsKey(entity.getSubject())
            & entity.getSubject()!=null
            & entity.getValue()!=null
            & entity.getDate()!=null
            & entity.getId()!=null) {
                Student student = dataBase.getStudents().get(studentId);
                if (student != null) {
                    Grade grade = student.getGrades().get(entity.getId());
                    if (grade != null)
                        return Response.status(403).build();
                    student.getGrades().add(entity);
                    return Response.status(201).entity(entity).build();
                }
        }
        return Response.status(400).build();
    }

    @DELETE
    @Path("{gradeId}")
    public Response deleteGrade(@PathParam("studentId") int studentId, @PathParam("gradeId") int gradeId){
        if (dataBase.getGrade(studentId, gradeId) != null){
            dataBase.getStudents().get(studentId).getGrades().remove(gradeId);
            return Response.status(200).build();
        }
        return Response.status(404).build();
    }
}




