import java.io.*;
import java.util.*;

public class RPSLeaderboard {
    public static Map<String, Integer> leaderboard = new HashMap<>();

    public static void loadBoard() {
        leaderboard.clear();
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

    private static String getPlace(int placeNum){
        if (placeNum == 1){ 
            return "1st";
        }
        if (placeNum == 2) {
            return "2nd";
        }
        if (placeNum == 3) {
            return "3rd";
        }
        else{
            return placeNum + "th";
        }
    }

    public static synchronized String getBoard(){
        StringBuilder sb = new StringBuilder();
        int place = 1;

        sb.append("\t      LEADERBOARD       \n");
        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(leaderboard.entrySet());
        sorted.sort((a,b) -> b.getValue() - a.getValue());

        for (Map.Entry<String, Integer> entry : sorted){
            String line = 
                getPlace(place) + " - " +
                entry.getKey() + " - " +
                entry.getValue() + " wins\n";
            
            sb.append(line);

            place++;
        }
        return sb.toString();
    }

    public static void displayLeaderboard() {

        System.out.println(getBoard());
    }
}
