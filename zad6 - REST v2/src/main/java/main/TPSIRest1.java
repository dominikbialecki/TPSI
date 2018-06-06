package main;

import resource.*;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class TPSIRest1 {

    public static void main(String[] args){


            URI baseUri = UriBuilder.fromUri("http://localhost/").port(9998).build();
            ResourceConfig config = new ResourceConfig()
                    .packages("resources")
                    .register(DeclarativeLinkingFeature.class);
            config.register(SubjectResource.class);
            config.register(GradeResource.class);
            config.register(StudentResource.class);
            HttpServer server = GrizzlyHttpServerFactory.createHttpServer(baseUri, config);

    }

}
