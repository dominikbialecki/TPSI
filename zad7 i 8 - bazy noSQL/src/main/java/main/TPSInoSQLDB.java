package main;

import Database.MongoDAO;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import resource.GradeResource;
import resource.StudentResource;
import resource.SubjectResource;
import resource.providers.CustomHeaders;
import resource.providers.DateParamConverterProvider;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class TPSInoSQLDB {

    public static void main(String[] args) {

        URI baseUri = UriBuilder.fromUri("http://localhost/").port(9999).build();
        ResourceConfig config = new ResourceConfig()
                .packages("resources")
                .register(new DateParamConverterProvider("yyyy-MM-dd"))
                .register(DeclarativeLinkingFeature.class)
                .register(CustomHeaders.class)
                .register(GradeResource.class)
                .register(SubjectResource.class)
                .register(StudentResource.class);
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(baseUri, config);
        MongoDAO.getInstance();
//        Logger l = Logger.getLogger("org.glassfish.grizzly.http.server.HttpHandler");
//        l.setLevel(Level.FINEST);
//        l.setUseParentHandlers(false);
//        ConsoleHandler ch = new ConsoleHandler();
//        ch.setLevel(Level.ALL);
//        l.addHandler(ch);
    }
}
