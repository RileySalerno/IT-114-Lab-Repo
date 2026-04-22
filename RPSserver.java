import java.net.*;
import java.util.*;
import java.io.*;

public class RPSserver {
    private static List<Player> queue = new ArrayList<>();
    private static Map<String, Player> privateRoom = new HashMap<>();

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("Server has started");

        while (true) {
            Socket listener = serverSocket.accept();
            Player player = new Player(listener);
            new Thread(player).start();
        }
    }

    static class Player implements Runnable {
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

        private void publicMatch() throws Exception{
            synchronized(queue) {
                queue.add(this);

                if (queue.size() >= 2){
                    Player p1 = queue.remove(0);
                    Player p2 = queue.remove(0);

                    new GameSession(p1, p2).gameStart();
                } else {
                    System.out.println("Waiting for another player to join");
                }
            }
        }
        private void createPR() throws Exception {
            System.out.println("Enter a Password using only numbers or letters");
            String roomPassword = incoming.readLine();

            synchronized (privateRoom) {
                privateRoom.put(roomPassword, this);
                System.out.println("Private lobby successfully created. Password is: " + roomPassword);
                System.out.println("Waiting for another Player to join the lobby");
            }
        }

        private void joinPR() throws Exception {
            System.out.println("Enter a Room Password to join");
            String password = incoming.readLine();

            if (privateRoom.containsKey(password)) {
                Player host = privateRoom.remove(password);
                new GameSession(host, this).gameStart();
            } else {
                System.out.println("Room dosent exist.");
            }
        }

        public int getMove() throws Exception{
            System.out.println("Enter Rock(1), Paper(2), or Scissors(3)");
            return Integer.parseInt(incoming.readLine());
        }

        public void send(String msg){
            System.out.println(msg);
        }

        static class GameSession {
            private Player p1;
            private Player p2;

            public GameSession(Player p1,Player p2) {
                this.p1 = p1;
                this.p2 = p2;
            }

            public void gameStart() throws Exception {
                p1.send("Matched with: ");
                p2.send("Matched with: ");

                int move1 = p1.getMove();
                int move2 = p2.getMove();

                String result = getResult(move1, move2);

                p1.send(result);
                p2.send(result);
            }

            private String getResult(int p1, int p2) {
                if (p1 == p2) return "Tie";
            if ((p1 == 1 && p2 == 3) ||
                (p1 == 2 && p2 == 1) ||
                (p1 == 3 && p2 == 2)) {
                return "Player 1 wins";
            }
            return "Player 2 wins";
            }
        }
    }
}
