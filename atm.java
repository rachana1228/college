import java.io.*;
import java.util.*;

public class ATM {
    private static final String ACCOUNTS_FILE = "accounts/accounts.txt";
    private static final String TRANSACTIONS_FILE = "transactions/transactions.txt";
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        System.out.println("=== Welcome to the ATM System ===");
        while (true) {
            System.out.println("\n1. Login");
            System.out.println("2. Create Account");
            System.out.println("3. Delete Account");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> login();
                case 2 -> createAccount();
                case 3 -> deleteAccount();
                case 4 -> {
                    System.out.println("Thank you for using the ATM. Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void login() throws IOException {
        System.out.print("Enter Account Number: ");
        String accNum = scanner.next();
        System.out.print("Enter PIN: ");
        String pin = scanner.next();

        if (authenticate(accNum, pin)) {
            System.out.println("Login successful!");
            userMenu(accNum);
        } else {
            System.out.println("Invalid credentials. Please try again.");
        }
    }

    private static boolean authenticate(String accNum, String pin) throws IOException {
        File file = new File(ACCOUNTS_FILE);
        if (!file.exists()) file.createNewFile(); // Ensure the file exists

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(accNum) && parts[1].equals(pin)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void userMenu(String accNum) throws IOException {
        while (true) {
            System.out.println("\n1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> checkBalance(accNum);
                case 2 -> deposit(accNum);
                case 3 -> withdraw(accNum);
                case 4 -> {
                    System.out.println("Logged out successfully.");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void checkBalance(String accNum) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(ACCOUNTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(accNum)) {
                    System.out.println("Your current balance is: ₹" + parts[2]);
                    return;
                }
            }
        }
    }

    private static void deposit(String accNum) throws IOException {
        System.out.print("Enter amount to deposit: ₹");
        double amount = scanner.nextDouble();
        if (amount <= 0) {
            System.out.println("Amount must be positive.");
            return;
        }
        updateBalance(accNum, amount);
        logTransaction(accNum, "Deposit", amount);
        System.out.println("Deposit successful!");
    }

    private static void withdraw(String accNum) throws IOException {
        System.out.print("Enter amount to withdraw: ₹");
        double amount = scanner.nextDouble();
        if (amount <= 0) {
            System.out.println("Amount must be positive.");
            return;
        }
        if (updateBalance(accNum, -amount)) {
            logTransaction(accNum, "Withdraw", amount);
            System.out.println("Withdrawal successful!");
        } else {
            System.out.println("Insufficient balance.");
        }
    }

    private static boolean updateBalance(String accNum, double amount) throws IOException {
        File inputFile = new File(ACCOUNTS_FILE);
        File tempFile = new File("accounts/temp.txt");

        boolean updated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(accNum)) {
                    double currentBalance = Double.parseDouble(parts[2]);
                    double newBalance = currentBalance + amount;

                    if (newBalance < 0) {
                        System.out.println("Insufficient balance.");
                        writer.write(line + "\n");  // Write original if not enough balance
                        return false;  // Exit with failure
                    }

                    writer.write(accNum + "," + parts[1] + "," + newBalance + "\n");
                    updated = true;
                } else {
                    writer.write(line + "\n");
                }
            }
        }

        if (!updated) {
            System.out.println("Account not found.");
            return false;
        }

        // Replace the old file with the updated temp file
        if (!tempFile.renameTo(inputFile)) {
            System.out.println("Error updating balance.");
            return false;
        }

        return true;
    }

    private static void logTransaction(String accNum, String type, double amount) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTIONS_FILE, true))) {
            writer.write(accNum + "," + type + "," + amount + "," + new Date() + "\n");
        }
    }

    private static void createAccount() throws IOException {
        System.out.print("Enter new Account Number: ");
        String accNum = scanner.next();
        System.out.print("Set a 4-digit PIN: ");
        String pin = scanner.next();

        if (pin.length() != 4 || !pin.matches("\\d+")) {
            System.out.println("PIN must be a 4-digit number.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ACCOUNTS_FILE, true))) {
            writer.write(accNum + "," + pin + ",0.0\n");
            System.out.println("Account created successfully!");
        }
    }

    private static void deleteAccount() throws IOException {
        System.out.print("Enter Account Number to delete: ");
        String accNum = scanner.next();
        System.out.print("Enter PIN: ");
        String pin = scanner.next();

        if (!authenticate(accNum, pin)) {
            System.out.println("Invalid credentials. Cannot delete account.");
            return;
        }

        File tempFile = new File("accounts/temp.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(ACCOUNTS_FILE));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith(accNum + ",")) {
                    writer.write(line + "\n");
                }
            }
        }
        if (!tempFile.renameTo(new File(ACCOUNTS_FILE))) {
            System.out.println("Error deleting account.");
        } else {
            System.out.println("Account deleted successfully.");
        }
    }
}
