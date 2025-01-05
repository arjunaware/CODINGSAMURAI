import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimpleATM extends JFrame {
    // Variables to store balance and user actions
    private double balance = 1000.0; // Default balance

    // Components
    private JLabel titleLabel;
    private JLabel balanceLabel;
    private JTextField amountField;
    private JButton checkBalanceButton, depositButton, withdrawButton, exitButton;

    // Constructor to build the UI
    public SimpleATM() {
        // Frame setup
        setTitle("Simple ATM System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 1)); // 6 rows for UI components

        // UI Components
        titleLabel = new JLabel("Welcome to Simple ATM", JLabel.CENTER);
        balanceLabel = new JLabel("Current Balance: $1000.0", JLabel.CENTER);
        amountField = new JTextField();
        amountField.setToolTipText("Enter amount here");
        checkBalanceButton = new JButton("Check Balance");
        depositButton = new JButton("Deposit Money");
        withdrawButton = new JButton("Withdraw Money");
        exitButton = new JButton("Exit");

        // Add components to the frame
        add(titleLabel);
        add(balanceLabel);
        add(amountField);
        add(checkBalanceButton);
        add(depositButton);
        add(withdrawButton);
        add(exitButton);

        // Action listeners for buttons
        checkBalanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Your Current Balance is: $" + balance);
            }
        });

        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = amountField.getText();
                if (!input.isEmpty() && isNumeric(input)) {
                    double amount = Double.parseDouble(input);
                    if (amount > 0) {
                        balance += amount;
                        balanceLabel.setText("Current Balance: $" + balance);
                        JOptionPane.showMessageDialog(null, "Successfully Deposited: $" + amount);
                        amountField.setText(""); // Clear the input field
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid Amount. Enter a positive value.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a valid amount.");
                }
            }
        });

        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = amountField.getText();
                if (!input.isEmpty() && isNumeric(input)) {
                    double amount = Double.parseDouble(input);
                    if (amount > 0 && amount <= balance) {
                        balance -= amount;
                        balanceLabel.setText("Current Balance: $" + balance);
                        JOptionPane.showMessageDialog(null, "Successfully Withdrawn: $" + amount);
                        amountField.setText(""); // Clear the input field
                    } else if (amount > balance) {
                        JOptionPane.showMessageDialog(null, "Insufficient Balance.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid Amount. Enter a positive value.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a valid amount.");
                }
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Thank you for using the ATM!");
                System.exit(0);
            }
        });

        // Set frame visibility
        setVisible(true);
    }

    // Utility method to check if input is numeric
    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Main method to run the program
    public static void main(String[] args) {
        new SimpleATM();
    }
}
