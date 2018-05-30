import com.cedarsoftware.util.io.JsonWriter;
import com.sun.net.httpserver.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class TPSIServer {
	public static void main(String[] args) throws Exception {
		int port = 8000;
		HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
		server.createContext("/", new RootHandler());
		server.createContext("/echo/", new EchoHandler());
        server.createContext("/redirect/", new RedirectHandler());
        server.createContext("/cookies/", new CookiesHandler());
        server.createContext("/auth/", new AuthHandler());
        HttpContext auth2Context = server.createContext("/auth2/", new Auth2Handler());
        auth2Context.setAuthenticator(new BasicAuthenticator("") {
            @Override
            public boolean checkCredentials(String user, String password) {
                return user.equals("test2") && password.equals("1234");
            }
        });

        System.out.println("Starting server on port: " + port);
		server.start();
	}

	static class RootHandler implements HttpHandler {
		public void handle(HttpExchange exchange) throws IOException {
			byte[] bytes = Files.readAllBytes(Paths.get("src/main/java/index.html"));

			exchange.getResponseHeaders().set("Content-Type", "text/html");
			exchange.sendResponseHeaders(200, bytes.length);
			OutputStream os = exchange.getResponseBody();
			os.write(bytes);
			os.close();
		}
	}

	static class EchoHandler implements HttpHandler {
		public void handle(HttpExchange exchange) throws IOException {

			Headers headers = exchange.getRequestHeaders();

			Map<String, Object> properties = new HashMap<>();
			properties.put(JsonWriter.PRETTY_PRINT, true);

			String json = JsonWriter.objectToJson(headers, properties);


			exchange.getResponseHeaders().set("Content-Type", "text/plain");
			exchange.sendResponseHeaders(200, json.length());
			OutputStream os = exchange.getResponseBody();
			os.write(json.getBytes());
			os.close();
		}
	}


    static class RedirectHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {

            exchange.getResponseHeaders().set("Location", "https://www.wp.pl/");
            exchange.sendResponseHeaders(301, -1);

        }
    }

	static class CookiesHandler implements HttpHandler {
		public void handle(HttpExchange exchange) throws IOException {

            Random generator = new Random();
            Integer cookie_value = generator.nextInt(10000);
            String cookie_name = "test_cookie";
            String cookie_expires = "; Expires=Wed, 30 Aug 2019 00:00:00 GMT";
            String cookie_path = "; Path=/echo/";

            String cookie_body = cookie_name + "=" + cookie_value.toString() + "; Domain=localhost" + cookie_path + cookie_expires;

            exchange.getResponseHeaders().set("Set-Cookie", cookie_body);
            exchange.sendResponseHeaders(200, -1);

            System.out.println(cookie_body);
            }
	}



    static class AuthHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {

            exchange.getResponseHeaders().set("Content-Type", "text/plain");

            Headers headers = exchange.getRequestHeaders();

            if (headers.containsKey("Authorization")){
                String authorizationHeader = headers.getFirst("Authorization");
                String encodedMessage = authorizationHeader.substring(6);
                String decodedMessage = new String(Base64.getDecoder().decode(encodedMessage));

                System.out.println(decodedMessage);
                if (decodedMessage.equals("test:1234")){
                    String response = "authorized";
                    exchange.sendResponseHeaders(200, response.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
                else {
                    exchange.getResponseHeaders().set("WWW-Authenticate", "Basic");
                    exchange.sendResponseHeaders(401, -1);

                }
            }
            else {
                exchange.getResponseHeaders().set("WWW-Authenticate", "Basic");
                exchange.sendResponseHeaders(401, -1);

            }


        }
    }

    static class Auth2Handler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            String response = "authorized";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();

        }
    }


}
