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

     private JPanel passwordPanel() {

        JPanel panel = new JPanel();

        JLabel label = new JLabel("Room Password:");

        passwordField = new JTextField(15);

        JButton submit = new JButton("Submit");

        submit.setFont(new Font("Arial", Font.BOLD, 18));

        submit.addActionListener(e -> {

            String password = passwordField.getText();

            out.println(pendingAction);

            out.println(password);

            cards.show(mainPanel, "waiting");
        });

        panel.add(label);
        panel.add(passwordField);
        panel.add(submit);

        return panel;
    }

    private JPanel gamePanel() {

        JPanel panel = new JPanel();

        panel.setLayout(new GridLayout(1, 3, 10, 10));


        ImageIcon rockIcon =new ImageIcon(getClass().getResource("/Icons/rock.png"));

        ImageIcon paperIcon =new ImageIcon(getClass().getResource("/Icons/paper.png"));

        ImageIcon ScissorsIcon =new ImageIcon(getClass().getResource("/Icons/Scissors.png"));

        JButton rock = new JButton(rockIcon);
        JButton paper = new JButton(paperIcon);
        JButton scissors = new JButton(ScissorsIcon);

        rock.setHorizontalTextPosition(SwingConstants.CENTER);
        rock.setVerticalTextPosition(SwingConstants.BOTTOM);

        paper.setHorizontalTextPosition(SwingConstants.CENTER);
        paper.setVerticalTextPosition(SwingConstants.BOTTOM);

        scissors.setHorizontalTextPosition(SwingConstants.CENTER);
        scissors.setVerticalTextPosition(SwingConstants.BOTTOM);

        Font buttonFont = new Font("Arial", Font.BOLD, 22);

        rock.setFont(buttonFont);
        paper.setFont(buttonFont);
        scissors.setFont(buttonFont);

        rock.setFocusPainted(false);
        paper.setFocusPainted(false);
        scissors.setFocusPainted(false);

        rock.setBorderPainted(false);
        paper.setBorderPainted(false);
        scissors.setBorderPainted(false);

        rock.setContentAreaFilled(false);
        paper.setContentAreaFilled(false);
        scissors.setContentAreaFilled(false);

        rock.addActionListener(e -> {

            out.println("1");

            cards.show(mainPanel, "waitingMove");
        });

        paper.addActionListener(e -> {

            out.println("2");

            cards.show(mainPanel, "waitingMove");
        });

        scissors.addActionListener(e -> {

            out.println("3");

            cards.show(mainPanel, "waitingMove");
        });

        panel.add(rock);
        panel.add(paper);
        panel.add(scissors);

        return panel;
    }

    private JPanel waitingPanel() {

        JPanel panel = new JPanel(new BorderLayout());

        JLabel label = new JLabel(
                "Waiting for opponent...",
                SwingConstants.CENTER);

        label.setFont(
                new Font("Arial", Font.BOLD, 26));

        panel.add(label, BorderLayout.CENTER);

        return panel;
    }

    private JPanel waitingMovePanel() {

        JPanel panel = new JPanel(new BorderLayout());

        JLabel label = new JLabel(
                "Waiting for opponent move...",
                SwingConstants.CENTER);

        label.setFont(
                new Font("Arial", Font.BOLD, 24));

        panel.add(label, BorderLayout.CENTER);

        return panel;
    }

    private JPanel resultPanel() {

        JPanel panel = new JPanel(new BorderLayout());

        resultLabel = new JLabel(
                "",
                SwingConstants.CENTER);

        resultLabel.setFont(
                new Font("Arial", Font.BOLD, 34));

        JLabel bottomText = new JLabel(
                "Play again?",
                SwingConstants.CENTER);

        bottomText.setFont(
                new Font("Arial", Font.BOLD, 22));

        JPanel buttons = new JPanel();

        JButton yes = new JButton("Yes");
        JButton no = new JButton("No");

        Font font = new Font("Arial", Font.BOLD, 20);

        yes.setFont(font);
        no.setFont(font);

        yes.addActionListener(e -> {

            out.println("yes");

            cards.show(mainPanel, "waiting");
        });

        no.addActionListener(e -> {

            out.println("no");

            cards.show(mainPanel, "waiting");
        });

        buttons.add(yes);
        buttons.add(no);

        panel.add(resultLabel, BorderLayout.CENTER);
        panel.add(bottomText, BorderLayout.NORTH);
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }
    
}
