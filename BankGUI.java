import javax.swing.*;
import java.awt.*;

public class BankGUI extends JFrame {

    private BankAccount account;
    private JLabel balance;
    private JTextField amountField;

    public BankGUI() {
        setupAccount(); 
        createDashboard(); 
    }

    private void setupAccount() {
        JTextField nameField = new JTextField(15);
        JTextField depositField = new JTextField(10);

        String[] accountTypes = {"Checking", "Savings"};
        JComboBox<String> accountTypeBox = new JComboBox<>(accountTypes);

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Initial Deposit:"));
        panel.add(depositField);
        panel.add(new JLabel("Account Type:"));
        panel.add(accountTypeBox);

        while (true) {
            int result = JOptionPane.showConfirmDialog(
                    this, panel, "Create a Bank Account", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
                );

        if (result != JOptionPane.OK_OPTION) {
            System.exit(0); 
        }

        try {
            String name = nameField.getText().trim();
            double initialDeposit = Double.parseDouble(depositField.getText());

            if (name.isEmpty() || initialDeposit < 0) {
                throw new NumberFormatException();
            }

            String selectedAccount = (String) accountTypeBox.getSelectedItem();

            if (selectedAccount.equals("Checking")) {
                account = new CheckingAccount(name, initialDeposit);
            } else {
                account = new SavingsAccount(name, initialDeposit);
            }

            break;

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                        this, "Please enter a valid name and positive number", "Error", JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void createDashboard() {
        setTitle("Bank Account Dashboard");
        setSize(500, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome, " + account.getAccountHolder() + "!");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(welcomeLabel, BorderLayout.NORTH);

        balance = new JLabel("Current Balance: $" + account.getBalance());
        balance.setHorizontalAlignment(SwingConstants.CENTER);
        balance.setFont(new Font("Arial", Font.PLAIN, 14));
        add(balance, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        amountField = new JTextField(10);
        inputPanel.add(new JLabel("Amount:"));
        inputPanel.add(amountField);

        JButton depositBtn = new JButton("Deposit");
        depositBtn.addActionListener(e -> handleDeposit());
        JButton withdrawBtn = new JButton("Withdraw");
        withdrawBtn.addActionListener(e -> handleWithdraw());
        JButton exit = new JButton("Exit");
        exit.setBackground(Color.RED);
        exit.setForeground(Color.WHITE);
        exit.addActionListener(e -> System.exit(0));

        inputPanel.add(depositBtn);
        inputPanel.add(withdrawBtn);
        inputPanel.add(exit);

        add(inputPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null); // Center window
        setVisible(true);
    }

    private void handleDeposit() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            String message = account.deposit(amount);
            balance.setText("Current Balance: $" + account.getBalance());
            JOptionPane.showMessageDialog(this, message);
            amountField.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error: Please enter a valid number", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleWithdraw() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            String message = account.withdraw(amount);
            balance.setText("Current Balance: $" + account.getBalance());
            JOptionPane.showMessageDialog(this, message);
            amountField.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error: Please enter a valid number", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BankGUI::new);
    }
}