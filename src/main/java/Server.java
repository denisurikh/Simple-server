package main.java;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static java.lang.Boolean.*;

public class Server {

    private static final boolean SHOW_REQUEST = FALSE;

    public static void main(String[] args) {

        try(ServerSocket serverSocket = new ServerSocket(80)) {
            System.out.println("Server is Up!");

            while (true) {
                Socket socket = serverSocket.accept();

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter writer = new PrintWriter(socket.getOutputStream())){

                    String firstString = reader.readLine();

                    if(SHOW_REQUEST) {
                        System.out.println(firstString);
                        while (reader.ready()) {
                                System.out.println(reader.readLine());
                        }

                    } else {
                        while (reader.ready()) {
                            reader.readLine();
                        }
                    }

                    if (firstString.matches("BREW /[Tt][Ee][Aa] HTTP/1.1")) {
                        writer.println("HTTP/1.1 200 OK");
                        writer.println("Connection: close");
                        writer.println("Content-Type: text/html; charset=UTF-8");
                        writer.println("");
                            printBody(writer);
                        writer.flush();

                    } else {
                        writer.println("HTTP/1.1 418 I am tea pod.");
                        writer.println("Connection: close");
                        writer.println("");
                        writer.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printBody(PrintWriter writer) {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Server.class
                .getClassLoader().getResourceAsStream("response.html")))){

            while (reader.ready()) {
                writer.println(reader.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
