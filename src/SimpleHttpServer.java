import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;

public class SimpleHttpServer {
    public static void main(String[] args) throws IOException {

       ServerSocket server = new ServerSocket(8080);

       while (true) {
           Socket client = server.accept();
           Thread thread = new Thread(() -> {
               try {
                   handleRequest(client);
               } catch (IOException e) {
                   e.getStackTrace();
               }
           });

           thread.start();
       }


    }

    public static void handleRequest(Socket client) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        OutputStream out = client.getOutputStream();

        String request = in.readLine();
        String[] requestParts = request.split(" ");
        String method = requestParts[0];
        String path = requestParts[1];


        if ("/".equals(path)) {
            String message = "<html><h1>Hello World!<h1></html>";
            byte[] responseBytes = message.getBytes();

            out.write("HTTP/1.1 200 OK\r\n".getBytes());
            out.write("Content-type: text/html\r\n".getBytes());
            out.write("Set-cookie: jamie=thebest\r\n".getBytes());
            out.write(("Content-Length: " + responseBytes.length + "\r\n").getBytes());
            out.write("\r\n".getBytes());
            out.write(responseBytes);

        } else {
            String message = "<html><h1>Not found<h1></html>";
            byte[] responseBytes = message.getBytes();

            out.write("HTTP/1.1 404.0 Not Found\r\n".getBytes());
            out.write("Content-type: text/html\r\n".getBytes());
            out.write(("Content-Length: " + responseBytes.length + "\r\n").getBytes());
            out.write("\r\n".getBytes());
            out.write(responseBytes);

        }


        out.close();
        in.close();
        client.close();


    }

}