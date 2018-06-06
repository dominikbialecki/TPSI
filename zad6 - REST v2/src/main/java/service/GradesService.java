package service;

import Database.DAOInterface;
import Database.GradesDAOFake;
import Database.SubjectDAOFake;
import model.Grade;
import model.Subject;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Collection;

public class GradesService extends Service {

    private DAOInterface<Grade> gradesDAO;

    public GradesService(String studentId) throws NotFoundException{
        int id;
        try {
            id = Integer.parseInt(studentId);
            this.gradesDAO = new GradesDAOFake(id);
        } catch (NumberFormatException e){
            throw new NotFoundException("wrong student's id format");
        }
    }


    public Collection<Grade> getGrades() {
        return gradesDAO.getCollection();
    }

    public Response getGrade(String gradeId) {
        Response.ResponseBuilder responseBuilder = Response.status(400);
        int intGradeId;
        try {
            intGradeId = Integer.parseInt(gradeId);
        } catch (NumberFormatException e){
            return responseBuilder.build();
        }
        Grade grade = gradesDAO.getData(intGradeId);
        if (grade != null) {
            return responseBuilder.status(200).entity(grade).build();
        }
        return responseBuilder.status(404).build();
    }

    public Response putGrade(Grade entity, String gradeId){
        Response.ResponseBuilder responseBuilder = Response.status(400);
        int intGradeId;
        try {
            intGradeId = Integer.parseInt(gradeId);
        } catch (NumberFormatException e){
            return responseBuilder.build();
        }
        Grade grade = gradesDAO.getData(intGradeId);
        if (grade != null) {
            responseBuilder.status(304);
            if (entity.getDate() != null) {
                grade.setDate(entity.getDate());
                responseBuilder.status(200);
            }
            if (entity.getSubject() != null) {
                grade.setSubject(entity.getSubject());
                responseBuilder.status(200);

            }
            if (entity.getValue() != null) {
                grade.setValue(entity.getValue());
                responseBuilder.status(200);
            }
            gradesDAO.updateData(grade);
        }
        return responseBuilder.build();
    }

    public Response postGrade(Grade entity){
        DAOInterface<Subject> subjectDAO= SubjectDAOFake.getInstance();
        if (!subjectDAO.containsData(entity.getSubject().getId())
                & entity.getSubject()!=null
                & entity.getValue()!=null
                & entity.getDate()!=null
                & entity.getId()!=null) {
            if (!gradesDAO.containsData(entity.getId()))
                return Response.status(403).build();
            gradesDAO.addData(entity);
            return Response.status(201).entity(entity).build();
        }
        return Response.status(400).build();
    }

    public Response deleteGrade(String gradeId){
        int intGradeId;
        try {
            intGradeId = Integer.parseInt(gradeId);
        } catch (NumberFormatException e){
            return Response.status(400).build();
        }
        if (gradesDAO.containsData(intGradeId)){
            gradesDAO.deleteData(intGradeId);
            return Response.status(200).build();
        }
        return Response.status(404).build();
    }
}
