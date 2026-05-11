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

        in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));

        out = new PrintWriter(
                socket.getOutputStream(), true);
    }

    private void setupGUI() {

        frame = new JFrame("RPS Online");

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

        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
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

        JPanel menuPanel = new JPanel();

        menuPanel.setLayout(new GridLayout(3, 1));

        JButton publicBtn = new JButton("Public Match");
        JButton createBtn = new JButton("Create Private Room");
        JButton joinBtn = new JButton("Join Private Room");

        Font font = new Font("", Font.BOLD, 20);

        publicBtn.setFont(font);
        createBtn.setFont(font);
        joinBtn.setFont(font);

        publicBtn.addActionListener(e -> {
            out.println("1");
        });

        createBtn.addActionListener(e -> {
            pendingAction = 2;
            cards.show(mainPanel, "password");
        });

        joinBtn.addActionListener(e -> {

            pendingAction = 3;

            cards.show(mainPanel, "password");
        });

        menuPanel.add(publicBtn);
        menuPanel.add(createBtn);
        menuPanel.add(joinBtn);

        return menuPanel;
    }

    private JPanel passwordPanel() {

        JPanel passPanel = new JPanel();

        JLabel label = new JLabel("Room Password:");

        passwordField = new JTextField(15);

        JButton submit = new JButton("Submit");

        submit.setFont(new Font("Arial", Font.BOLD, 18));

        submit.addActionListener(e -> {

            String password = passwordField.getText();

            if (!password.matches("\\d{1,10}")) {
                JOptionPane.showMessageDialog(frame, "Password can only be 10 or less numbers");
                return;
            }

            out.println(pendingAction);

            out.println(password);

            cards.show(mainPanel, "waiting");
        });

        passPanel.add(label);
        passPanel.add(passwordField);
        passPanel.add(submit);

        return passPanel;
    }

    private JPanel gamePanel() {

        JPanel panel = new JPanel();

        panel.setLayout(new GridLayout(1, 3, 10, 10));

        ImageIcon rockIcon = new ImageIcon(getClass().getResource("/Icons/rock.png"));

        ImageIcon paperIcon = new ImageIcon(getClass().getResource("/Icons/paper.png"));

        ImageIcon ScissorsIcon = new ImageIcon(getClass().getResource("/Icons/Scissors.png"));

        JButton rock = new JButton(rockIcon);
        JButton paper = new JButton(paperIcon);
        JButton scissors = new JButton(ScissorsIcon);

        rock.setHorizontalTextPosition(SwingConstants.CENTER);
        rock.setVerticalTextPosition(SwingConstants.BOTTOM);

        paper.setHorizontalTextPosition(SwingConstants.CENTER);
        paper.setVerticalTextPosition(SwingConstants.BOTTOM);

        scissors.setHorizontalTextPosition(SwingConstants.CENTER);
        scissors.setVerticalTextPosition(SwingConstants.BOTTOM);

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

        JPanel waitpanel = new JPanel(new BorderLayout());

        JLabel label = new JLabel("Waiting for opponent", SwingConstants.CENTER);

        label.setFont(new Font("Arial", Font.BOLD, 26));

        waitpanel.add(label, BorderLayout.CENTER);

        return waitpanel;
    }

    private JPanel waitingMovePanel() {

        JPanel waitMovepanel = new JPanel(new BorderLayout());

        JLabel label = new JLabel("Waiting for opponents move", SwingConstants.CENTER);
        label.setFont(new Font("Roboto", Font.BOLD, 24));

        waitMovepanel.add(label, BorderLayout.CENTER);

        return waitMovepanel;
    }

    private JPanel resultPanel() {

        JPanel panel = new JPanel(new BorderLayout());

        resultLabel = new JLabel("", SwingConstants.CENTER);

        resultLabel.setFont(new Font("Roboto", Font.BOLD, 34));
        JLabel bottomText = new JLabel("Play again?", SwingConstants.CENTER);

        bottomText.setFont(new Font("Roboto", Font.BOLD, 22));

        JPanel buttons = new JPanel();

        JButton yes = new JButton("Yes");
        JButton no = new JButton("No");

        Font font = new Font("Arial", Font.BOLD, 20);

        yes.setFont(font);
        no.setFont(font);

        yes.addActionListener(e -> {
            out.println("yes");
            resultLabel.setText("Waiting for opponent");
        });

        no.addActionListener(e -> {
            out.println("no");
            resultLabel.setText("");
            cards.show(mainPanel, "menu");
        });

        buttons.add(yes);
        buttons.add(no);

        panel.add(resultLabel, BorderLayout.CENTER);
        panel.add(bottomText, BorderLayout.NORTH);
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    private void startListenerThread() {
        new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    final String message = msg;
                    SwingUtilities.invokeLater(() -> handleMessage(message));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();
    }

    private void handleMessage(String msg) {

        System.out.println("SERVER: " + msg);
        if (msg.contains("Matched with")) {
            cards.show(mainPanel, "game");
        }

        if (msg.contains("Tie")) {
            resultLabel.setText("Tie");
            cards.show(mainPanel, "result");
        }

        if (msg.contains("wins")) {
            if (msg.startsWith(username + " wins")) {
                resultLabel.setText("You Win");
            } else {
                resultLabel.setText("You Lose");
            }
            cards.show(mainPanel, "result");
        }

        if (msg.equals("RETURN_TO_LOBBY")) {
            SwingUtilities.invokeLater(() -> {
                resultLabel.setText("");
                cards.show(mainPanel, "menu");
                frame.revalidate();
                frame.repaint();
            });
        }

        if (msg.contains("Username is already being used")) {
            JOptionPane.showMessageDialog(frame, "Username is already being used");
            cards.show(mainPanel, "login");
        }

        if (msg.contains("Username must")) {

            JOptionPane.showMessageDialog(frame, "Username can only have 10 characters");
            cards.show(mainPanel, "login");
        }

        if (msg.contains("Username can only")) {
            JOptionPane.showMessageDialog(frame, "Username can only have letters and numbers allowed");
            cards.show(mainPanel, "login");
        }

        if (msg.contains("Invalid password")) {
            JOptionPane.showMessageDialog(frame, "Invalid password.");
            cards.show(mainPanel, "menu");
        }

        if (msg.contains("Room doesn't exist")) {
            JOptionPane.showMessageDialog(frame, "Room doesn't exist.");
            cards.show(mainPanel, "menu");
        }

        if (msg.contains("Opponent Disconnected")) {
            resultLabel.setText("Opponent Disconnected");
            cards.show(mainPanel, "result");
        }

        if (msg.equals("START_GAME")) {
            cards.show(mainPanel, "game");
        }

        if (msg.equals("Waiting for opponent")) {
            SwingUtilities.invokeLater(() -> {
                cards.show(mainPanel, "waiting");
            });
        }
    }
}
