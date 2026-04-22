import java.net.*;
import java.util.*;
import java.io.*;

public class RPSserver {
    private static List<Player> playerQueue = new ArrayList<>();
    private static Map<String, Player> privateRoom = new HashMap<>();

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("Server started...");

        while (true) {
            Socket listener = serverSocket.accept();
            Player player = new Player(listener);
            new Thread(player).start();
        }
    }
    
    static class Player {
        private Socket listener;
        private BufferedReader incoming;
        private PrintWriter outgoing;

        public Player(Socket listener) throws Exception{
            this.listener = listener;
            incoming = new BufferedReader(new InputStreamReader(listener.getInputStream()));
            outgoing = new PrintWriter(listener.getOutputStream(), true);
        }

         public void run() {
            try {
                outgoing.println("Enter 1 for Public Match, 2 to Create Private Room, 3 to Join Private Room:");
                int roomType = Integer.parseInt(incoming.readLine());

                if (roomType == 1) {
                    publicMatch();
                } else if (roomType == 2) {
                    createPR();
                } else if (roomType == 3) {
                    joinPR();
                }

            } catch (Exception e) {
                System.out.println("Client disconnected.");
            }
        }
}
