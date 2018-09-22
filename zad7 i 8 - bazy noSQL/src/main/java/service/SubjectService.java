package service;

import Database.GradesDAO;
import Database.StudentDAO;
import Database.SubjectDAO;
import model.Student;
import model.Subject;
import org.bson.types.ObjectId;

import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.stream.Collectors;

public class SubjectService extends Service {

    private SubjectDAO subjectDAO = SubjectDAO.getInstance();

    public Collection<Subject> getSubjects(String professor, String name) {
        return subjectDAO.getCollection(professor, name);
    }

    public Response getSubject(ObjectId id) {
        Subject subject = subjectDAO.getData(id);
        if (subject == null) {
            return Response.status(404).build();
        }
        return Response.ok().entity(subject).build();
    }

    public Response putSubject(Subject entity, ObjectId id) {
        entity.setId(id);
        Subject subject = subjectDAO.getData(entity.getId());
        if (subject == null) {
            return Response.status(404).build();
        }
        if (subject.equals(entity)) {
            return Response.notModified().build();
        }
        subjectDAO.updateData(subject);
        return Response.ok().build();


    }

    public Response postSubject(Subject entity, String path) {
        if (entity.getProfessor() != null && entity.getName() != null) {
            Subject subject = subjectDAO.addData(entity);
            try {
                return Response.created(new URI(path + subject.getId())).build();
            } catch (URISyntaxException e) {
                return Response.status(400).build();
            }
        }
        return Response.status(400).build();
    }

    public Response deleteSubject(ObjectId id) {
        Response.ResponseBuilder builder = Response.status(404);
        if (subjectDAO.containsData(id)) {
            removeGradesWithSubjectId(id);
            subjectDAO.deleteData(id);
            builder.status(200);
        }
        return builder.build();
    }

    private void removeGradesWithSubjectId(ObjectId subjectID) {
        StudentDAO studentDAO = StudentDAO.getInstance();
        studentDAO.getCollection(null, null, null, null, null).forEach( student -> {
            int initSize = student.getGrades().size();
            student.getGrades().values().removeIf(grade ->
                grade.getSubject().getId().equals(subjectID)
            );
            if (initSize != student.getGrades().size()) {
                studentDAO.updateData(student);
            }
        });
    }

}
