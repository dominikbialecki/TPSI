import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TPSIRest1 {

    public static void main(String[] args){
            DataBase dataBase = DataBase.getInstance();
            createData(dataBase);


            URI baseUri = UriBuilder.fromUri("http://localhost/").port(9998).build();
            ResourceConfig config = new ResourceConfig(SubjectResource.class);
            config.register(StudentResource.class);
            config.register(GradeResource.class);
            HttpServer server = GrizzlyHttpServerFactory.createHttpServer(baseUri, config);

    }

    public static void createData(DataBase dataBase){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Student student1 = new Student("Dominik", "Bialecki", dateFormat.parse("03-09-1995"));
            Student student2 = new Student("Irena", "Nowak", dateFormat.parse("26-11-2006"));
            Student student3 = new Student("Jan", "Kowalski",dateFormat.parse("14-06-1992"));
            dataBase.addStudent(student1);
            dataBase.addStudent(student2);
            dataBase.addStudent(student3);
            Subject subject1 = new Subject("Analiza Matematyczna", "Stefan Banach");
            Subject subject2 = new Subject("Algebra Liniowa", "Małgorzata Zygora");
            dataBase.addSubject(subject1);
            dataBase.addSubject(subject2);
            Grade grade1 = new Grade(5f, new Date(), subject1);
            ArrayList<Grade> gradesS1 = new ArrayList<Grade>();
            gradesS1.add(grade1);
            student1.setGrades(gradesS1);

        } catch (Exception e){
            e.printStackTrace();
            System.out.println("error while creating data");
        }

    }

}
