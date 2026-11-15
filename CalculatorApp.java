import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CalculatorApp extends Application {

    private TextField display;
    private double firstOperand = 0;
    private String operator = "";
    private boolean startNewNumber = true;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Calculator App");

        // 1. Setup the Display TextField
        display = new TextField();
        display.setEditable(false);
        display.setAlignment(Pos.CENTER_RIGHT);
        display.setStyle("-fx-font-size: 22px; -fx-padding: 10px;");

        // 2. Setup the GridPane for Buttons
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);

        // Define button layout
        String[][] buttons = {
            {"7", "8", "9", "/"},
            {"4", "5", "6", "*"},
            {"1", "2", "3", "-"},
            {"0", "C", "=", "+"}
        };

        // Populate the GridPane with buttons
        for (int row = 0; row < buttons.length; row++) {
            for (int col = 0; col < buttons[row].length; col++) {
                String text = buttons[row][col];
                Button btn = createCalculatorButton(text);
                gridPane.add(btn, col, row);
            }
        }

        // 3. Combine components into a main VBox layout
        VBox root = new VBox(15, display, gridPane);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #f4f4f4;");

        // 4. Scene and Stage configuration
        Scene scene = new Scene(root, 300, 380);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    // Helper method to create styled buttons and attach event handlers
    private Button createCalculatorButton(String text) {
        Button btn = new Button(text);
        btn.setPrefSize(60, 60);
        btn.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Attach event handler to process button clicks
        btn.setOnAction(e -> handleButtonPress(text));
        return btn;
    }

    // Core logic for handling user input and calculations
    private void handleButtonPress(String text) {
        if (text.matches("[0-9]")) {
            // Handle digit input
            if (startNewNumber) {
                display.setText(text);
                startNewNumber = false;
            } else {
                display.setText(display.getText() + text);
            }
        } else if (text.matches("[+\\-*/]")) {
            // Handle operator input
            try {
                firstOperand = Double.parseDouble(display.getText());
                operator = text;
                startNewNumber = true;
            } catch (NumberFormatException e) {
                display.setText("Error");
            }
        } else if (text.equals("=")) {
            // Handle equals calculation
            try {
                double secondOperand = Double.parseDouble(display.getText());
                double result = calculate(firstOperand, secondOperand, operator);

                if (Double.isNaN(result) || Double.isInfinite(result)) {
                    display.setText("Cannot divide by zero");
                } else {
                    // Format output to drop unnecessary decimal zeroes for integers
                    if (result == (long) result) {
                        display.setText(String.valueOf((long) result));
                    } else {
                        display.setText(String.valueOf(result));
                    }
                }
                startNewNumber = true;
                operator = "";
            } catch (Exception e) {
                display.setText("Error");
            }
        } else if (text.equals("C")) {
            // Handle Clear button
            display.clear();
            firstOperand = 0;
            operator = "";
            startNewNumber = true;
        }
    }

    // Arithmetic operation helper
    private double calculate(double a, double b, String op) {
        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/": 
                if (b == 0) return Double.NaN; // Triggers division by zero error check
                return a / b;
            default: return b;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}