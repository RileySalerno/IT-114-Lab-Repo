public class CheckingAccount extends BankAccount {
    // DO NOT CHANGE ANY VARIABLE, METHOD, OR CLASS NAMES. THE AUTOGRADER DEPENDS ON THEM.
    private final double OVERDRAFT_FEE = 35.00;

    public CheckingAccount(String accountHolder, double initialDeposit) {
        super(accountHolder, initialDeposit);
    }

    @Override
    public String withdraw(double amount) {
        if (amount <= 0) {
            return "Please enter a valid number";
        }
        // TODO: Implement Checking withdrawal logic
        double newBalance = getBalance() - amount;

        if (amount > getBalance()) {
            newBalance -= OVERDRAFT_FEE;
            return "Overdraft! $35.00 fee. New Balance: " + getBalance();
        }

        setBalance(newBalance);
        return "New Balance: $" + getBalance();
    }
}