package service;

import Database.DAOInterface;
import Database.SubjectDAOFake;
import model.Subject;

import javax.ws.rs.core.Response;
import java.util.Collection;

public class SubjectService extends Service {

    private DAOInterface<Subject> subjectDAO = SubjectDAOFake.getInstance();

    public Collection<Subject> getSubjects() {
        return subjectDAO.getCollection();
    }

    public Response getSubject(String id) {
        Response.ResponseBuilder builder = Response.status(200);
        int subjectId;
        try {
            subjectId = convertId(id);
        } catch (NumberFormatException e){
            return builder.status(400).build();
        }
        Subject subject = subjectDAO.getData(subjectId);
        if (subject == null){
            return builder.status(404).build();
        }
        return builder.entity(subject).build();
    }

    public Response putSubject(Subject entity, String id) {
        Response.ResponseBuilder builder = Response.status(404);
        int subjectId;
        try {
            subjectId = convertId(id);
        } catch (NumberFormatException e){
            return builder.status(400).build();
        }
        Subject subject = subjectDAO.getData(subjectId);
        if (subject != null) {
            builder.status(304).build();

            if (entity.getName() != null) {
                subject.setName(entity.getName());
                builder.status(200);
            }
            if (entity.getProfessor() != null) {
                subject.setProfessor(entity.getProfessor());
                builder.status(200);
            }
            subjectDAO.updateData(subject);
        }
        return builder.build();
    }

    public Response postSubject(Subject entity){
        if (subjectDAO.containsData(entity.getId()))
            return Response.status(403).build();
        if (entity.getProfessor()!=null & entity.getName()!=null){
            subjectDAO.addData(entity);
            return Response.status(201).entity(entity).build();
        }
        return Response.status(400).build();
    }

    public Response deleteSubject(String id){
        Response.ResponseBuilder builder = Response.status(404);
        int subjectId;
        try {
            subjectId = convertId(id);
        } catch (NumberFormatException e) {
            return Response.status(400).build();
        }
        if (subjectDAO.containsData(subjectId)) {
            builder.status(200);
            subjectDAO.deleteData(subjectId);
        }
        return builder.build();

    }

}
