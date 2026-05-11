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

     private JPanel loginPanel() {

        JPanel panel = new JPanel();

        JLabel label = new JLabel("Username:");

        JTextField nameField = new JTextField(15);

        JButton connect = new JButton("Connect");

        connect.setFont(new Font("Arial", Font.BOLD, 18));

        connect.addActionListener(e -> {

            username = nameField.getText();

            out.println(username);

            cards.show(mainPanel, "menu");
        });

        panel.add(label);
        panel.add(nameField);
        panel.add(connect);

        return panel;
    }

    private JPanel menuPanel() {

        JPanel panel = new JPanel();

        JButton publicBtn = new JButton("Public Match");
        JButton createBtn = new JButton("Create Private");
        JButton joinBtn = new JButton("Join Private");

        Font font = new Font("Arial", Font.BOLD, 20);

        publicBtn.setFont(font);
        createBtn.setFont(font);
        joinBtn.setFont(font);

        publicBtn.addActionListener(e -> {

            out.println("1");

            cards.show(mainPanel, "waiting");
        });

        createBtn.addActionListener(e -> {

            pendingAction = 2;

            cards.show(mainPanel, "password");
        });

        joinBtn.addActionListener(e -> {

            pendingAction = 3;

            cards.show(mainPanel, "password");
        });

        panel.add(publicBtn);
        panel.add(createBtn);
        panel.add(joinBtn);

        return panel;
    }

}
