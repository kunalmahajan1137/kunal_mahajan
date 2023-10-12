import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

public class ScientificCalculator {
    private JFrame frame;
    private JTextField displayField;
    private JPanel buttonPanel;
    private Stack<String> inputStack;

    public ScientificCalculator() {
        frame = new JFrame("Scientific Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new BorderLayout());

        displayField = new JTextField();
        displayField.setHorizontalAlignment(JTextField.RIGHT);
        frame.add(displayField, BorderLayout.NORTH);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 5));
        frame.add(buttonPanel, BorderLayout.CENTER);

        inputStack = new Stack<>();

        addButton("7");
        addButton("8");
        addButton("9");
        addButton("/");
        addButton("sqrt");
        addButton("4");
        addButton("5");
        addButton("6");
        addButton("*");
        addButton("^");
        addButton("1");
        addButton("2");
        addButton("3");
        addButton("-");
        addButton("sin");
        addButton("0");
        addButton(".");
        addButton("=");
        addButton("+");
        addButton("cos");
        addButton("tan");

        frame.setVisible(true);
    }

    private void addButton(String label) {
        JButton button = new JButton(label);
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        buttonPanel.add(button);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String buttonText = button.getText();
                if (Character.isDigit(buttonText.charAt(0)) || buttonText.equals(".")) {
                    displayField.setText(displayField.getText() + buttonText);
                } else if (buttonText.equals("=")) {
                    calculate();
                } else if (buttonText.equals("sqrt")) {
                    double value = Double.parseDouble(displayField.getText());
                    double result = Math.sqrt(value);
                    displayField.setText(String.valueOf(result));
                } else if (buttonText.equals("^")) {
                    inputStack.push(displayField.getText());
                    inputStack.push("^");
                    displayField.setText("");
                } else if (buttonText.equals("sin")) {
                    double value = Double.parseDouble(displayField.getText());
                    double result = Math.sin(Math.toRadians(value));
                    displayField.setText(String.valueOf(result));
                } else if (buttonText.equals("cos")) {
                    double value = Double.parseDouble(displayField.getText());
                    double result = Math.cos(Math.toRadians(value));
                    displayField.setText(String.valueOf(result));
                } else if (buttonText.equals("tan")) {
                    double value = Double.parseDouble(displayField.getText());
                    double result = Math.tan(Math.toRadians(value));
                    displayField.setText(String.valueOf(result));
                } else {
                    if (!inputStack.isEmpty()) {
                        inputStack.push(displayField.getText());
                        inputStack.push(buttonText);
                        displayField.setText("");
                    }
                }
            }
        });
    }

    private void calculate() {
        if (!inputStack.isEmpty()) {
            inputStack.push(displayField.getText());
            double result = evaluateExpression(inputStack);
            displayField.setText(String.valueOf(result));
            inputStack.clear();
        }
    }

    private double evaluateExpression(Stack<String> inputStack) {
        Stack<Double> operandStack = new Stack<>();
        Stack<String> operatorStack = new Stack<>();

        while (!inputStack.isEmpty()) {
            String token = inputStack.pop();
            if (isNumeric(token)) {
                operandStack.push(Double.parseDouble(token));
            } else if (token.equals("^")) {
                double exponent = operandStack.pop();
                double base = operandStack.pop();
                double result = Math.pow(base, exponent);
                operandStack.push(result);
            } else if (isOperator(token)) {
                while (!operatorStack.isEmpty() && precedence(operatorStack.peek()) >= precedence(token)) {
                    applyOperator(operandStack, operatorStack.pop());
                }
                operatorStack.push(token);
            }
        }

        while (!operatorStack.isEmpty()) {
            applyOperator(operandStack, operatorStack.pop());
        }

        return operandStack.pop();
    }

    private void applyOperator(Stack<Double> operandStack, String operator) {
        double operand2 = operandStack.pop();
        double operand1 = operandStack.pop();
        switch (operator) {
            case "+":
                operandStack.push(operand1 + operand2);
                break;
            case "-":
                operandStack.push(operand1 - operand2);
                break;
            case "*":
                operandStack.push(operand1 * operand2);
                break;
            case "/":
                if (operand2 != 0) {
                    operandStack.push(operand1 / operand2);
                } else {
                    displayField.setText("Error");
                    inputStack.clear();
                }
                break;
        }
    }

    private int precedence(String operator) {
        switch (operator) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            case "^":
                return 3;
            default:
                return 0;
        }
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isOperator(String str) {
        return str.equals("+") || str.equals("-") || str.equals("*") || str.equals("/") || str.equals("^");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ScientificCalculator();
            }
        });
    }
}
