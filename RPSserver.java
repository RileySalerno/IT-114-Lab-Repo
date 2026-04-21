import java.net.*;
import java.io.*;

public class RPSserver {
    public static void main(String[] args) throws Exception{
        int portNum = 8080;
        ServerSocket server = new ServerSocket(portNum);
        try {
                System.out.println("");
                Socket clientSocket = server.accept();
                System.out.println("Player has Connected");
                BufferedReader in = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter( clientSocket.getOutputStream(), true);
            System.out.println("Welcome to RPS Multiplayer");
        } catch (Exception e) {
            System.out.println("There was an error in the server: " + e.getMessage());
        }
    }
}
