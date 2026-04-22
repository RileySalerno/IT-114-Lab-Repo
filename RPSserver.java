import java.net.*;
import java.util.*;
import java.io.*;

public class RPSserver {
    private static List<Player> queue = new ArrayList<>();
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
                send("Enter 1 for Public Match, 2 to Create Private Room, 3 to Join Private Room:");
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

        public void send(String msg){
            outgoing.println(msg);
        }

        private void publicMatch() throws Exception{
            synchronized(queue) {
                queue.add(this);

                if (queue.size() >= 2){
                    Player p1 = queue.remove(0);
                    Player p2 = queue.remove(0);

                    new GameSession(p1, p2).gameStart();
                } else {
                    send("Waiting for another player to join");
                }
            }
        }
        private void createPR() throws Exception {
            outgoing.println("Enter a Password using only numbers or letters");
            String roomPassword = incoming.readLine();

            synchronized (privateRoom) {
                privateRoom.put(roomPassword, this);
                send("Private lobby successfully created. Password is: " + roomPassword);
                send("Waiting for another Player to join the lobby");
            }
        }

        private void joinPR() throws Exception {
            send("Enter a Room Password to join");
            String password = incoming.readLine();

            if (privateRoom.containsKey(password)) {
                Player host = privateRoom.remove(password);
                send("Youve joined a Private room");
                new GameSession(host, this).gameStart();
            } else {
                send("Room dosent exist.");
            }
        }

        public int getMove() throws Exception{
            send("Enter Rock(1), Paper(2), or Scissors(3)");
            return Integer.parseInt(incoming.readLine());
        }


        static class GameSession {
            private Player p1;
            private Player p2;

            public GameSession(Player p1,Player p2) {
                this.p1 = p1;
                this.p2 = p2;
            }

            public void gameStart() throws Exception {
                while (true) {
                    p1.send("Matched with opponent");
                    p2.send("Matched with opponent");

                    int move1 = p1.getMove();
                    int move2 = p2.getMove();

                    String result = getResult(move1, move2);

                    p1.send(result);
                    p2.send(result);

                    p1.send("Play again? (yes/no)");
                    p2.send("Play again? (yes/no)");

                    String r1 = p1.incoming.readLine();
                    String r2 = p2.incoming.readLine();

                    if (!r1.equalsIgnoreCase("yes") || !r2.equalsIgnoreCase("yes")) {
                        p1.send("Game ended.");
                        p2.send("Game ended.");
                        break;
                    }
                }
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
