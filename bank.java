import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class BankGUI extends JFrame implements ActionListener {
    private Map<Integer, Account> accounts = new HashMap<>();
    private JTextField accountNumberField, accountHolderField, balanceField, amountField;
    private JTextArea outputArea;

    public BankGUI() {
        setTitle("Bank Account Management System");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));

        JLabel accountNumberLabel = new JLabel("Account Number:");
        accountNumberField = new JTextField();
        JLabel accountHolderLabel = new JLabel("Account Holder:");
        accountHolderField = new JTextField();
        JLabel balanceLabel = new JLabel("Balance:");
        balanceField = new JTextField();
        JLabel amountLabel = new JLabel("Amount:");
        amountField = new JTextField();

        JButton createAccountButton = new JButton("Create Account");
        createAccountButton.addActionListener(this);
        JButton depositButton = new JButton("Deposit");
        depositButton.addActionListener(this);
        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.addActionListener(this);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        panel.add(accountNumberLabel);
        panel.add(accountNumberField);
        panel.add(accountHolderLabel);
        panel.add(accountHolderField);
        panel.add(balanceLabel);
        panel.add(balanceField);
        panel.add(amountLabel);
        panel.add(amountField);
        panel.add(createAccountButton);
        panel.add(depositButton);
        panel.add(withdrawButton);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Create Account")) {
            int accountNumber = Integer.parseInt(accountNumberField.getText());
            String accountHolder = accountHolderField.getText();
            double balance = Double.parseDouble(balanceField.getText());
            Account account = new Account(accountNumber, accountHolder, balance);
            accounts.put(accountNumber, account);
            displayMessage("Account created successfully.");
        } else if (e.getActionCommand().equals("Deposit")) {
            int accountNumber = Integer.parseInt(accountNumberField.getText());
            double amount = Double.parseDouble(amountField.getText());
            Account account = accounts.get(accountNumber);
            if (account != null) {
                account.deposit(amount);
                displayMessage("Deposit successful. Current balance: " + account.getBalance());
            } else {
                displayMessage("Account not found.");
            }
        } else if (e.getActionCommand().equals("Withdraw")) {
            int accountNumber = Integer.parseInt(accountNumberField.getText());
            double amount = Double.parseDouble(amountField.getText());
            Account account = accounts.get(accountNumber);
            if (account != null) {
                if (account.withdraw(amount)) {
                    displayMessage("Withdrawal successful. Current balance: " + account.getBalance());
                } else {
                    displayMessage("Insufficient funds.");
                }
            } else {
                displayMessage("Account not found.");
            }
        }
    }

    private void displayMessage(String message) {
        outputArea.append(message + "\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BankGUI bankGUI = new BankGUI();
            bankGUI.setVisible(true);
        });
    }

    private class Account {
        private int accountNumber;
        private String accountHolder;
        private double balance;

        public Account(int accountNumber, String accountHolder, double balance) {
            this.accountNumber = accountNumber;
            this.accountHolder = accountHolder;
            this.balance = balance;
        }

        public int getAccountNumber() {
            return accountNumber;
        }

        public String getAccountHolder() {
            return accountHolder;
        }

        public double getBalance() {
            return balance;
        }

        public void deposit(double amount) {
            balance += amount;
        }

        public boolean withdraw(double amount) {
            if (balance >= amount) {
                balance -= amount;
                return true;
            }
            return false;
        }
    }
}
