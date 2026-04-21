import javax.swing.*;
import java.awt.*;

public class RPS_GUI extends JFrame{
    public static void main(String[] args) {
        String username = JOptionPane.showInputDialog("Enter a username");
        JOptionPane.showMessageDialog(null, "Hello " + username);

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
    }
}