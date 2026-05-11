import java.net.*;
import java.util.*;
import java.io.*;

public class RPSserver {
    private static List<Player> queue = new ArrayList<>();
    private static Map<String, Player> privateRoom = new HashMap<>();
    private static List<String> activePlayers = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        RPSLeaderboard.loadBoard();
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("Started Server");

        new Thread(() -> {
            while (true){
                try{
                    Player p1 = null;
                    Player p2 = null;

                    synchronized (queue) {
                        if (queue.size() >= 2){
                            p1 = queue.remove(0);
                            p2 = queue.remove(0);
                        }
                    }

                    if (p1 != null && p2 != null){
                        final Player fp1 = p1;
                        final Player fp2 = p2;
                        new Thread(() -> {
                            try {
                                new Player.GameSession(fp1, fp2).gameStart();
                            }catch(Exception e){
                                System.out.println("Game Session error: " + e.getMessage());
                            }
                        }).start();
                    }
                    Thread.sleep(100);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

         while (true) {
            Socket listener = serverSocket.accept();
            Player player = new Player(listener);
            new Thread(player).start();
        }
    }

    static class Player implements Runnable {
        private BufferedReader incoming;
        private PrintWriter outgoing;
        private String name;

        public Player(Socket listener) throws Exception {
            incoming = new BufferedReader(new InputStreamReader(listener.getInputStream()));
            outgoing = new PrintWriter(listener.getOutputStream(), true);
        }

        public String getName() {
            return name;
        }

        public void run(){
            try {
                System.out.println("Client has joined");
                while (true) {
                    send("Enter a Username thats 10 or less characters(letters and numbers only)");
                    String enteredName = incoming.readLine();
                    boolean userExists = false;
                    synchronized (activePlayers) {
                        if (enteredName.isEmpty()) {
                            send("Please enter a username!");
                            continue;
                        }

                        if (enteredName.length() > 10) {
                            send("Username must be 10 or less characters");
                            continue;
                        }

                        if(!enteredName.matches("[a-zA-Z0-9]+")){
                            send("Username can only have numbers and letters");
                            continue;
                        }

                        for(String user : activePlayers){
                            if(user.equalsIgnoreCase(enteredName)){
                                userExists = true;
                                break;
                            }
                        }

                        if (userExists) {
                            send("Username is already being used");
                            continue;
                        }

                        name = enteredName;
                        activePlayers.add(name);
                        break;
                    }
                }

                send(name + " has joined the server");
                send("Enter 1 for Public Match, 2 to Create Private Room, 3 to Join Private Room:");
                String input = incoming.readLine();
                if (input == null) {
                    return;
                }
                int roomType;
                try{
                    roomType = Integer.parseInt(input);
                } catch (NumberFormatException e){
                    send("You can only enter 1, 2, or 3");
                    return;
                }
                if (roomType == 1) {
                    publicMatch();
                } else if (roomType == 2) {
                    createPR();
                } else if (roomType == 3) {
                    joinPR();
                }

            } catch (Exception e) {
                System.out.println("Client disconnected.");
            } finally{
                if (name != null){
                    synchronized(activePlayers){
                        activePlayers.remove(name);
                    }
                }
            }
        }

        public void send(String msg) {
            outgoing.println(msg);
        }

        private void publicMatch() throws Exception {
            synchronized (queue) {
                queue.add(this);
                send("Waiting for opponent");
            }
        }

        private void createPR() throws Exception {
            outgoing.println("Enter a Password using only numbers or letters");
            String roomPassword = incoming.readLine();

            if (roomPassword == null || !roomPassword.matches("[0-9]+")){
                send("Invalid password");
                return;
            }

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
                send("You've joined a Private room");
                new GameSession(host, this).gameStart();
            } else {
                send("Room dosent exist.");
            }
        }

        public int getMove() throws Exception {
            while (true){
                String input = incoming.readLine();
                    if (input == null){
                        throw new IOException("Client disconnected");
                    }

                try {
                    int move = Integer.parseInt(input);
                    if (move == 1 || move == 2 || move == 3){
                        return move;
                    }else{
                        send("invalid input. Only enter 1, 2, or 3");
                    }
                } catch (NumberFormatException e){
                    send("Invalid input, Enter 1, 2, or 3");
                }
            }
        }

        static class GameSession {
            private Player p1;
            private Player p2;

            public GameSession(Player p1, Player p2) {
                this.p1 = p1;
                this.p2 = p2;
            }

            public void gameStart() throws Exception {
                while (true) {
                    p1.send("Matched with " + p2.getName());
                    p2.send("Matched with " + p1.getName());

                    p1.send("Enter 1(Rock), 2(Paper), or 3(Scissors)");
                    p2.send("Enter 1(Rock), 2(Paper), or 3(Scissors)");

                    int move1 = p1.getMove();
                    int move2 = p2.getMove();

                    String result = getResult(move1, move2);

                    p1.send(result);
                    p2.send(result);

                    String board = RPSLeaderboard.getBoard();

                    p1.send(board);
                    p2.send(board);

                    p1.send("Play again? (yes/no)");
                    p2.send("Play again? (yes/no)");

                    String r1 = p1.incoming.readLine();
                    String r2 = p2.incoming.readLine();

                    if (r1 == null || r2 == null) {
                        p1.send("Opponent Disconnected");
                        p2.send("Opponent Disconnected");
                        break;
                    }

                    if (r1.equalsIgnoreCase("no") || r2.equalsIgnoreCase("no")) {
                        p1.send("Exited Server");
                        p2.send("Exited Server");
                        break;
                    }
                }
            }

            private String getResult(int move1, int move2) {
                if (move1 == move2)
                    return "Tie";
                if ((move1 == 1 && move2 == 3) || (move1 == 2 && move2 == 1) || (move1 == 3 && move2 == 2)) {
                    RPSLeaderboard.addWins(p1.getName());
                    return p1.getName() + " wins";
                }
                RPSLeaderboard.addWins(p2.getName());
                return p2.getName() + " wins";
            }
        }
    }
}
