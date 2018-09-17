package service;

import Database.DAOInterface;
import Database.StudentDAOFake;
import model.Student;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

public class StudentService extends Service {

    private DAOInterface<Student> studentDAO = StudentDAOFake.getInstance();

    public Response getStudent( String id) {
        Response.ResponseBuilder builder = Response.status(200);
        int studentId;
        try {
            studentId = convertId(id);
        } catch (NumberFormatException e) {
            return builder.status(400).build();
        }
        Student student = studentDAO.getData(studentId);

        if (student == null)
            builder.status(Response.Status.NOT_FOUND);
        else builder.entity(student);

        return builder.build();
    }


    public Collection<Student> getStudents() {
        return studentDAO.getCollection();
    }


    public Response postStudent(Student entity, String path) {
        if (entity.getName() != null && entity.getSurname() != null && entity.getBirthDate() != null) {
            Student student = studentDAO.addData(entity);
            try {
                return Response.created(new URI(path+student.getIndex())).build();
            } catch (URISyntaxException e) {
                return Response.status(400).build();
            }
        }
        return Response.status(400).build();
    }


    public Response putStudent(Student entity, String id) {
        Response.ResponseBuilder builder = Response.status(404);
        int studentId;
        try {
            studentId = convertId(id);
        } catch (NumberFormatException e) {
            return Response.status(400).build();
        }
        Student student = studentDAO.getData(studentId);
        if (student != null) {
            builder.status(304);
            if (entity.getName() != null) {
                student.setName(entity.getName());
                builder.status(200);
            }
            if (entity.getSurname() != null) {
                student.setSurname(entity.getSurname());
                builder.status(200);
            }
            if (entity.getBirthDate() != null) {
                student.setBirthDate(entity.getBirthDate());
                builder.status(200);
            }
            studentDAO.updateData(student);
        }
        return builder.build();
    }

    @DELETE
    public Response deleteStudent(String id) {
        Response.ResponseBuilder builder = Response.status(404);
        int studentId;
        try {
            studentId = convertId(id);
        } catch (NumberFormatException e) {
            return Response.status(400).build();
        }
        if (studentDAO.containsData(studentId)) {
            studentDAO.deleteData(studentId);
            builder.status(200);
        }
        return builder.build();
    }
}
