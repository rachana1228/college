import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class EnhancedChatbot extends JFrame {
    private JTextArea chatArea; // To display chat history
    private JTextField inputField; // To take user input
    private JButton sendButton; // To send the message
    private Random random; // For generating random responses

    public EnhancedChatbot() {
        setTitle("Enhanced Chatbot");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Chat area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        // Input field and button
        JPanel panel = new JPanel();
        inputField = new JTextField(20);
        sendButton = new JButton("Send");
        panel.add(inputField);
        panel.add(sendButton);
        add(panel, BorderLayout.SOUTH);

        random = new Random();

        // Send button action listener
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // Input field action listener
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        setVisible(true);
    }

    // Method to send and process the user's message
    private void sendMessage() {
        String userInput = inputField.getText().trim();
        if (!userInput.isEmpty()) {
            chatArea.append("You: " + userInput + "\n");
            inputField.setText(""); // Clear input field
            String response = getResponse(userInput);
            chatArea.append("Chatbot: " + response + "\n\n");
        }
    }

    // Method to generate chatbot responses
    private String getResponse(String input) {
        input = input.toLowerCase();
        
        // Check for arithmetic questions
        if (input.contains("what is") || input.contains("calculate")) {
            return calculateArithmetic(input);
        } else if (input.contains("hello") || input.contains("hi")) {
            return "Hello! How can I assist you today?";
        } else if (input.contains("how are you")) {
            return "I'm just a program, but I'm doing well!";
        } else if (input.contains("your name") || input.contains("who are you")) {
            return "I am an Enhanced Chatbot, here to help you!";
        } else if (input.contains("weather")) {
            return "I'm not connected to the internet, but I hope the weather is nice!";
        } else if (input.contains("joke")) {
            return tellJoke();
        } else if (input.contains("bye") || input.contains("goodbye")) {
            return "Goodbye! Have a great day!";
        } else {
            return "I didn't quite understand that. Can you ask something else?";
        }
    }

    // Method to calculate arithmetic expressions
    private String calculateArithmetic(String input) {
        try {
            String[] parts = input.split(" ");
            double num1 = Double.parseDouble(parts[2]); // First number
            String operator = parts[3]; // Operator
            double num2 = Double.parseDouble(parts[4]); // Second number

            switch (operator) {
                case "+":
                    return String.valueOf(num1 + num2);
                case "-":
                    return String.valueOf(num1 - num2);
                case "*":
                case "x":
                case "times":
                    return String.valueOf(num1 * num2);
                case "/":
                    if (num2 != 0) {
                        return String.valueOf(num1 / num2);
                    } else {
                        return "Division by zero is not allowed.";
                    }
                default:
                    return "I can only do basic arithmetic (+, -, *, /).";
            }
        } catch (Exception e) {
            return "Please provide a valid arithmetic question like 'What is 2 + 2?'.";
        }
    }

    // Method to tell a joke
    private String tellJoke() {
        String[] jokes = {
            "Why did the computer go to the doctor? Because it had a virus!",
            "Why do programmers prefer dark mode? Because light attracts bugs!",
            "Why was the Java developer so bad at soccer? Because he kept kicking the ball into the null!"
        };
        return jokes[random.nextInt(jokes.length)];
    }

    public static void main(String[] args) {
        new EnhancedChatbot(); // Create game instance
    }
}
