public class SavingsAccount extends BankAccount {
    // DO NOT CHANGE ANY VARIABLE, METHOD, OR CLASS NAMES. THE AUTOGRADER DEPENDS ON THEM.
    public SavingsAccount(String accountHolder, double initialDeposit) {
        // TODO: Call the parent constructor using 'super'
        super(accountHolder, initialDeposit);
    }

    @Override
    public String withdraw(double amount) {
        if (amount < 0) {
            return "Please enter a valid number";
        }
        if (amount > getBalance()) {
            return "Transaction Denied: Insufficient funds";
        } else {
            setBalance(getBalance() - amount);
        }

        return "New Balance: $" + getBalance();
    }
}