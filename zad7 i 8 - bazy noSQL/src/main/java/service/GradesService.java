package service;

import Database.GradesDAO;
import Database.StudentDAO;
import Database.SubjectDAO;
import model.Grade;
import model.Subject;
import org.bson.types.ObjectId;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Date;

public class GradesService extends Service {

    private GradesDAO gradesDAO = GradesDAO.getInstance();
    private StudentDAO studentDAO = StudentDAO.getInstance();

    private void checkIfStudentExists(int studentIndex) throws NotFoundException {
        if (!studentDAO.containsData(studentIndex)) {
            throw new NotFoundException("Student with this ID does not exist");
        }
    }

    public Collection<Grade> getGrades(int studentIndex,
                                       ObjectId subjectId,
                                       String minValueS,
                                       String maxValueS,
                                       Date dateFrom,
                                       Date dateTo) {
        Float minValue;
        Float maxValue;
        try {
            minValue = (minValueS == null) ? null : Float.parseFloat(minValueS);
            maxValue = (maxValueS == null) ? null : Float.parseFloat(maxValueS);
        } catch (Exception e) {
            throw new BadRequestException("Wrong value format");
        }
        checkIfStudentExists(studentIndex);
        return gradesDAO.getCollection(studentIndex, subjectId, minValue, maxValue, dateFrom, dateTo);
    }

    public Response getGrade(int studentIndex, int gradeId) {
        checkIfStudentExists(studentIndex);
        Response.ResponseBuilder responseBuilder = Response.status(400);
        Grade grade = gradesDAO.getData(studentIndex, gradeId);
        if (grade == null) {
            return responseBuilder.status(404).build();
        }
        return responseBuilder.status(200).entity(grade).build();
    }

    public Response putGrade(int studentIndex, Grade entity, int gradeId) {
        checkIfStudentExists(studentIndex);
        entity.setId(gradeId);
        Grade grade = gradesDAO.getData(studentIndex, gradeId);
        if (grade == null) {
            return Response.status(404).build();
        }
        if (grade.equals(entity)) {
            return Response.status(304).build();
        }
        gradesDAO.updateData(studentIndex, grade);
        return Response.ok().build();
    }

    public Response postGrade(int studentIndex, Grade entity, String path) {
        if (entity.getValue() % 0.5 != 0 || entity.getValue() <2 || entity.getValue() >5) throw new BadRequestException("Wrong value format");
        checkIfStudentExists(studentIndex);
        if (entity.getSubject() != null) {
            Subject existingSubject = SubjectDAO.getInstance().getData(entity.getSubject().getId());
            if (existingSubject != null
                    && entity.getValue() != null
                    && entity.getDate() != null) {
                entity.setSubject(existingSubject);
                Grade newGrade = gradesDAO.addData(studentIndex, entity);
                try {
                    return Response.created(new URI(path + newGrade.getId())).build();
                } catch (URISyntaxException e) {
                    return Response.status(400).build();
                }
            }
        }
        return Response.status(400).build();
    }

    public Response deleteGrade(int studentIndex, int gradeId) {
        checkIfStudentExists(studentIndex);
        if (gradesDAO.containsData(studentIndex, gradeId)) {
            gradesDAO.deleteData(studentIndex, gradeId);
            return Response.status(200).build();
        }
        return Response.status(404).build();
    }
}
