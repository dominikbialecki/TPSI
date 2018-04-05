import com.sun.net.httpserver.*;

import java.io.*;
import org.apache.commons.io.IOUtils;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class TPSIproxy2 {

    public static BlackList blackList = new BlackList();
    public static Statistics statistics = new Statistics();

    public static void main(String[] args) throws Exception {
        int port = 8000;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new RootHandler());
        System.out.println("Starting server on port: " + port);
        server.start();
    }



    static class RootHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {


            statistics.readDataFromFile();
            //class collecting statistics for this request
            ConnectionData connectionData = new ConnectionData();


            //getting request info
            URI uri = exchange.getRequestURI();
            Headers headers = exchange.getRequestHeaders();
            String method = exchange.getRequestMethod();
            URL url = new URL(uri.toString());


//            System.out.println(url.toString());


            //check if URL is in blacklist
            blackList.getBlackList();
            if (blackList.contains(url.toString())){
                System.out.println("Forbidden URL");
                exchange.sendResponseHeaders(403, -1);
            }
            else{
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                //set up connection properties
                connection.setAllowUserInteraction(true);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                //connection.setFollowRedirects(true);
                connection.setRequestMethod(method);

                for (String headerName : headers.keySet()){
                    for (String headerValue : headers.get(headerName)){
                        connection.addRequestProperty(headerName, headerValue);
                    }
                }
                byte[] requestBytes = new byte[0];
                if (exchange.getRequestHeaders().containsKey("Content-Length")) {
                    if ((Integer.parseInt(exchange.getRequestHeaders().get("Content-Length").get(0)))>=0) {
                        InputStream is = exchange.getRequestBody();
                        OutputStream os = connection.getOutputStream();
                        requestBytes = IOUtils.toByteArray(is);
                        os.write(requestBytes);
                        is.close();
                        os.close();
                    }
                }

                //connecting
//                connection.connect();
//                System.out.println("Connecting...");

                //getting the response
                //headers
                Map<String, List<String>> responseHeaders = connection.getHeaderFields();
                for (String headerName : responseHeaders.keySet()) {
                    if (headerName != null) {
                        exchange.getRequestHeaders().put(headerName, responseHeaders.get(headerName));
                    }
                }


                //response code
                int contentLength = 0;
                try {
                    if (responseHeaders.containsKey("Content-Length")) {
                        String contentLengthStr = responseHeaders.get("Content-Length").get(0);
                        contentLength = Integer.parseInt(contentLengthStr);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
//                System.out.println("content length: "+contentLength);

                exchange.sendResponseHeaders(connection.getResponseCode(), contentLength);
                System.out.println(connection.getResponseCode());

                //message body
                byte[] bytes;
                if (200 <= connection.getResponseCode() && connection.getResponseCode() < 300) {
                    InputStream is = connection.getInputStream();
                    bytes = IOUtils.toByteArray(is);
                    OutputStream os = exchange.getResponseBody();
                    os.write(bytes);
                    is.close();
                    os.close();
                } else {
                    InputStream es = connection.getErrorStream();
                    bytes = IOUtils.toByteArray(es);
                    OutputStream os = exchange.getResponseBody();
                    os.write(bytes);
                    es.close();
                    os.close();
                }

                connectionData.url = exchange.getRequestHeaders().get("Host").get(0);
                connectionData.dataSend = (long)requestBytes.length;
                connectionData.dataGot = (long)bytes.length;


                statistics.insertData(connectionData);
                System.out.println(connectionData.toString());
//                System.out.println(exchange.getRequestMethod().toString()+" "+statistics.get(connectionData).toString());

            }
        }
    }


}







