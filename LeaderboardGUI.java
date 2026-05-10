import javax.swing.*;
import java.awt.*;

public class LeaderboardGUI extends JFrame {

    private JTextArea textArea;

    public LeaderboardGUI() {

        setTitle("RPS Leaderboard");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        textArea = new JTextArea();
        textArea.setFont(new Font("Papyrus", Font.BOLD, 16));
        textArea.setEditable(false);

        add(new JScrollPane(textArea));

        RPSLeaderboard.loadBoard();


        javax.swing.Timer timer = new javax.swing.Timer(1000, e -> updateBoard());

        timer.start();

        updateBoard();

        setVisible(true);
    }

    private void updateBoard() {

        RPSLeaderboard.loadBoard();

        String boardText = RPSLeaderboard.getBoard();

        textArea.setText(boardText);
    }

    public static void main(String[] args) {

        new LeaderboardGUI();
    }
}