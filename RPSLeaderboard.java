import java.io.*;
import java.util.*;

public class RPSLeaderboard {
    public static Map<String, Integer> leaderboard = new HashMap<>();

    public static void loadBoard() {
        File file = new File("leaderboard.txt");

        if(!file.exists()) return;

        try (Scanner scan = new Scanner(file)){
            while(scan.hasNextLine()){
                String line = scan.nextLine();

                String[] parts = line.split(":");

                leaderboard.put(parts[0],Integer.parseInt(parts[1]));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void saveBoard() {
        try (PrintWriter writer = new PrintWriter("leaderboard.txt")){
            for (Map.Entry<String, Integer> entry : leaderboard.entrySet()){
                writer.println(entry.getKey() + ":" + entry.getValue());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static synchronized void addWins(String username){
        leaderboard.put(username, leaderboard.getOrDefault(username, 0) + 1);
        saveBoard();
    }
}
