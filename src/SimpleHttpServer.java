import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class SimpleHttpServer {
    public static void main(String[] args) throws IOException {

        try (ServerSocket server = new ServerSocket(8000)) {

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


    }

    public static void handleRequest(Socket client) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        OutputStream out = client.getOutputStream();
        String request = in.readLine();
        String[] requestParts = request.split(" ");
        String method = requestParts[0];
        String path = requestParts[1];

        if ("/".equals(path)) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                BufferedReader html = new BufferedReader(new FileReader("src/helloWorld.html"));
                String line = html.readLine();

                while (line != null) {
                    stringBuilder.append(line);
                    line = html.readLine();
                }
                html.close();
            } catch (IOException e){
                e.printStackTrace();
            }

            String content = stringBuilder.toString();
            byte[] responseBytes = content.getBytes();

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