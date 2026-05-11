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

     private void setupConnection() throws Exception {
        Socket socket = new Socket("localhost", 8080);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }


}
