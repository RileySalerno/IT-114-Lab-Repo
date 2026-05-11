import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class RPS_GUI {

    private JFrame frame;
    private CardLayout cards;
    private JPanel mainPanel;

    private BufferedReader in;
    private PrintWriter out;

    private JLabel resultLabel;

    private String username;

    private JPanel passwordPanel;
    private JTextField passwordField;
    private int pendingAction;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new RPS_GUI().start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    public void start() throws Exception {
        setupConnection();
        setupGUI();
        startListenerThread();
    }

     private void setupConnection() throws Exception {
        Socket socket = new Socket("localhost", 8080);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    private void setupGUI() {
        frame = new JFrame("Rock Paper Scissors");
        cards = new CardLayout();
        mainPanel = new JPanel(cards);

        mainPanel.add(loginPanel(), "login");
        mainPanel.add(menuPanel(), "menu");
        mainPanel.add(gamePanel(), "game");
        mainPanel.add(waitingPanel(), "waiting");
        mainPanel.add(waitingMovePanel(), "waitingMove");
        mainPanel.add(resultPanel(), "result");
        mainPanel.add(passwordPanel(), "password");

        frame.add(mainPanel);
        frame.setSize(420, 260);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        cards.show(mainPanel, "login");
    }


}
