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
}
