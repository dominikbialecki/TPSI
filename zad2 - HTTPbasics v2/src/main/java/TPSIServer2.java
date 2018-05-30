import com.sun.net.httpserver.*;

import java.io.*;

import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;


public class TPSIServer2 {

    private static String root;

    public static void main(String[] args) throws Exception {

        if (args.length == 0) {
            System.out.println("Directory path was not provided!");
        } else if (args.length > 1) {
            System.out.println("You can enter only one path!");
        } else {
            root = args[0];
            if (!new File(root).isDirectory()) {
                System.out.println("Specified path is not a directory!");
            } else {
                int port = 8000;
                HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
                server.createContext("/", new RootHandler());
                System.out.println("Starting server on port: " + port);
                server.start();
            }
        }
    }

    //check if second file is a forefather od the first file
    static boolean pathTraversalDetection(File parent, File file) {
        try { return file.getCanonicalPath().startsWith(parent.getCanonicalPath()); }
        catch (IOException e) { e.printStackTrace(); }
        return false;
    }


    static class RootHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {

        try {
            //create File object representing file specified in URI
            URI URI = exchange.getRequestURI();
            File currentFile = new File(root, URI.getPath());


            //?????????????????????????????????????????????????????????????????????????????????????????????
            try {
                System.out.println("CanRead? :" + currentFile.canRead());
            } catch (SecurityException e) {
                System.out.println("Error: " + e.getMessage());
            }


            //Wrong URI
            //uzyc open
            if (!currentFile.exists()) {
                exchange.sendResponseHeaders(404, -1);
            }
            //Directory traversal attack prevention
            else if (!pathTraversalDetection(new File(root), currentFile)) {
                exchange.sendResponseHeaders(403, -1);
            }
            //URI points on a directory
            else if (currentFile.isDirectory()) {
                exchange.getResponseHeaders().set("Content-Type", "text/html");

                StringBuilder htmlBuilder = new StringBuilder();
                htmlBuilder.append("<!DOCTYPE html>");
                htmlBuilder.append("<html>");
                htmlBuilder.append("<head><title>" + currentFile.getName() + "</title></head>");
                htmlBuilder.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
                htmlBuilder.append("<link href=\"https://fonts.googleapis.com/icon?family=Material+Icons\" rel=\"stylesheet\">");
                htmlBuilder.append("<body>");

                File[] directoryFiles = currentFile.listFiles();

                for (int i = 0; i < directoryFiles.length; i++) {
                    if (directoryFiles[i].isDirectory()) {
                        htmlBuilder.append("<i class=\"material-icons\">folder</i>");
                    } else {
                        htmlBuilder.append("<i class=\"material-icons\">file_download</i>");
                    }
                    htmlBuilder.append("<a href=\"http://localhost:8000");
                    htmlBuilder.append(URI.getPath());
                    if (!URI.toString().equals("/")) {
                        htmlBuilder.append("/");
                    }
                    htmlBuilder.append(directoryFiles[i].getName());
                    htmlBuilder.append("\">");
                    htmlBuilder.append(directoryFiles[i].getName());
                    htmlBuilder.append("</a><br>");
                }
                htmlBuilder.append("</body>");
                htmlBuilder.append("</html>");
                String html = htmlBuilder.toString();

                exchange.sendResponseHeaders(200, html.length());

                OutputStream os = exchange.getResponseBody();
                os.write(html.getBytes());
                os.close();
            }
            //URI points on a file
            else if (currentFile.isFile()) {
                byte[] currentFileBytes = Files.readAllBytes(Paths.get(currentFile.getPath()));

                exchange.getResponseHeaders().set("Content-Type", "application/octet-stream");
                exchange.getResponseHeaders().set("Content-Disposition", "attachment; filename=\"" + currentFile.getName() + "\"");
                exchange.sendResponseHeaders(200, currentFileBytes.length);
                OutputStream os = exchange.getResponseBody();
                os.write(currentFileBytes);
                os.close();
            }
            //This should never happen
            else {
                System.out.println("Something went wrong");
                exchange.sendResponseHeaders(400, -1);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        }
    }
}