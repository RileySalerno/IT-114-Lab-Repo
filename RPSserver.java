import java.net.*;
import java.util.*;
import java.io.*;

public class RPSserver {

    private static List<Player> queue = new ArrayList<>();
    private static Map<String, Player> privateRoom = new HashMap<>();
    private static List<String> activePlayers = new ArrayList<>();

    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("Server started");

        new Thread(() -> {
            while (true) {
                try {
                    Player p1 = null;
                    Player p2 = null;

                    synchronized (queue) {
                        if (queue.size() >= 2) {
                            p1 = queue.remove(0);
                            p2 = queue.remove(0);
                        }
                    }

                    if (p1 != null && p2 != null) {
                        Player finalP1 = p1;
                        Player finalP2 = p2;

                        new Thread(() -> {
                            try {
                                new Player.GameSession(finalP1, finalP2).gameStart();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }).start();
                    }

                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        while (true) {
            Socket socket = serverSocket.accept();
            Player player = new Player(socket);
            new Thread(player).start();
        }
    }

    static class Player implements Runnable {

        private BufferedReader incoming;
        private PrintWriter outgoing;
        private String name;

        public Player(Socket socket) throws Exception {
            incoming = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outgoing = new PrintWriter(socket.getOutputStream(), true);
        }

        public String getName() {
            return name;
        }

        public void run() {
            try {

                while (true) {
                    send("Enter username:");
                    String enteredName = incoming.readLine();

                    synchronized (activePlayers) {

                        if (enteredName == null || enteredName.isEmpty()) {
                            send("Username required");
                            continue;
                        }

                        if (enteredName.length() > 10) {
                            send("Username must be 10 or less characters");
                            continue;
                        }

                        if (!enteredName.matches("[a-zA-Z0-9]+")) {
                            send("Username can only have numbers and letters");
                            continue;
                        }

                        if (activePlayers.contains(enteredName)) {
                            send("Username is already being used");
                            continue;
                        }

                        name = enteredName;
                        activePlayers.add(name);
                        break;
                    }
                }
                

                send("Welcome " + name);

                String mode = incoming.readLine();

                if (mode == null) return;

                int type = Integer.parseInt(mode);

                if (type == 1) publicMatch();
                if (type == 2) createPR();
                if (type == 3) joinPR();

            } catch (Exception e) {
                System.out.println("Client disconnected");
            } finally {
                if (name != null) {
                    activePlayers.remove(name);
                }
            }
        }

        public void send(String msg) {
            outgoing.println(msg);
        }

        private void publicMatch() {
            synchronized (queue) {
                queue.add(this);
                send("Waiting for opponent...");
            }
        }

        private void createPR() throws Exception {
            String password = incoming.readLine();

            if (password == null || !password.matches("\\d{1,10}")) {
                send("Invalid password");
                return;
            }

            synchronized (privateRoom) {
                privateRoom.put(password, this);
                send("Private room created");
            }

            synchronized(this){
                wait();
            }
        }

        private void joinPR() throws Exception {
            String password = incoming.readLine();
            Player host;

            synchronized (privateRoom) {
                    host = privateRoom.remove(password);
            }
            if (host == null){
                send("Room dosen't exist");
                return;
            }
            send("Joined private room");

            new Thread(() -> {
                try{
                    new GameSession(host, this).gameStart();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }).start();

            synchronized(this){
                wait();
            }
        }

        public int getMove() throws Exception {
            while (true) {
                String input = incoming.readLine();
                int move = Integer.parseInt(input);
                if (move >= 1 && move <= 3) return move;
            }
        }

        static class GameSession {
            Player p1, p2;

            GameSession(Player p1, Player p2) {
                this.p1 = p1;
                this.p2 = p2;
            }

            void gameStart() throws Exception {
                while (true) {

                    p1.send("Matched with " + p2.getName());
                    p2.send("Matched with " + p1.getName());

                    int m1 = p1.getMove();
                    int m2 = p2.getMove();

                    String result;

                    if (m1 == m2) {
                        result = "Tie";
                    } else if ((m1 == 1 && m2 == 3) ||
                               (m1 == 2 && m2 == 1) ||
                               (m1 == 3 && m2 == 2)) {
                        result = p1.getName() + " wins";
                    } else {
                        result = p2.getName() + " wins";
                    }

                    p1.send(result);
                    p2.send(result);

                    String r1 = p1.incoming.readLine();
                    String r2 = p2.incoming.readLine();

                    if (r1 == null || r2 == null) break;
                    if (r1.equals("no") || r2.equals("no")) break;
                }
            }
        }
    }
}
