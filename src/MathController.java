import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.mariuszgromada.math.mxparser.*;
import javafx.scene.input.KeyCode;

import javafx.animation.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import static java.lang.Math.abs;

public class MathController {

    private final int targetNumber;
    private final TextField textInput;
    private LinkedList<Integer> numberSet;

    private Stack<LinkedList<Integer>> undoStack;

    private StackPane mathPane;

    private UIController uiController;
    private Label debugLabel;


    public MathController(StackPane mathPane, UIController uiController) {
        this.uiController = uiController;
        this.mathPane = mathPane;
        this.undoStack = new Stack<>();
        this.numberSet = new LinkedList<>();
        this.targetNumber = (int) (Math.random() * 101);
        for (int i = 0; i < 5; i++) {
            int setRandom = (int) (Math.random() * 13);
            this.numberSet.add(setRandom);
        }
        this.undoStack.push(new LinkedList<>(this.numberSet));
        this.textInput = new TextField();
        this.textInput.setAlignment(Pos.CENTER);
        this.textInput.setFocusTraversable(true);
        VBox inputPane = new VBox(10, this.textInput);
        inputPane.setMaxWidth(300);
        inputPane.setPadding(new Insets(250,0,0,0));
        this.mathPane.getChildren().add(inputPane);
        createDebugPanel();


        setDebugLabel("Your target number is " + this.targetNumber);
        setDebugLabel("Your number set is " + this.numberSet);
        setDebugLabel("Try to get as close as possible. Good luck! You can use 'undo' and 'quit'.");
        actionEvent();



    }

    private void actionEvent() {
        this.textInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                callInput();
            } else if (event.getCode() == KeyCode.ESCAPE) {
                this.uiController.showOptionsPane();
                setDebugLabel("escape");
            }
        });
    }


    private void callInput() {
        String input = this.textInput.getText();
        Scanner scanner = new Scanner(input);

        while (scanner.hasNextLine()) {
            input = scanner.nextLine();
                Expression e = new Expression(input);
                if (validateExpression(input)) {
                    calculateSet(input, e);
                }
            }
            this.textInput.clear();
        }



    public void calculateSet(String expression, Expression e) {
        expression = expression.replaceAll("\\s+", "");
        String[] tokens = expression.split("[-+*/^()]+");

        for (String token : tokens) {
            if (isNumeric(token)) {
                int num = Integer.parseInt(token);
                if (this.numberSet.contains(num)) {
                    this.numberSet.removeFirstOccurrence(num);
                }
            }
        }
        this.numberSet.add((int) e.calculate());
        this.undoStack.push(new LinkedList<>(this.numberSet));

        if (this.numberSet.size() > 1) {
            this.uiController.setSetLabel("Number Set: " + this.numberSet);

            setDebugLabel("Your new set is " + this.numberSet + " | Reminder: Target = " + this.targetNumber);
        } else {
            int targetDifference = abs((int) (this.targetNumber - e.calculate()));

            if (targetDifference == 0) {
                setDebugLabel("Perfect Score! Your number " + e.calculate() + " is " + targetDifference + " away from the target number " + this.targetNumber + "!");

            } else {

                this.uiController.setSetLabel("Your number " + e.calculate() + " is " + targetDifference + " away from the target number " + this.targetNumber + ".");
            }
            new MathController(mathPane, this.uiController);

        }
    }


    public boolean validateExpression(String expression) {
        expression = expression.replaceAll("\\s+", "");
        String[] tokens = expression.split("(?<=\\^)|(?=\\^)|(?<=\\+)|(?=\\+)|(?<=-)|(?=-)|(?<=\\*)|(?=\\*)|(?<=/)|(?=/)|(?<=\\()|(?=\\()|(?<=\\))|(?=\\))");

        boolean hasNumericValue = false;
        boolean hasOperator = false;
        boolean lastTokenIsNumeric = false;

        for (String token : tokens) {
            if (isNumeric(token)) {
                int num = Integer.parseInt(token);
                if (!this.numberSet.contains(num)) {
                    setDebugLabel("Invalid input.");
                    wobble(this.textInput);
                    return false;
                }
                hasNumericValue = true;
                lastTokenIsNumeric = true;
            } else if (isOperator(token)) {
                hasOperator = true;
                if ("/".equals(token)) {
                    if (lastTokenIsNumeric && "0".equals(tokens[tokens.length - 1])) {
                        setDebugLabel("Invalid input.");
                        wobble(this.textInput);
                        return false;
                    }
                }
                lastTokenIsNumeric = false;
            } else {
                setDebugLabel("Invalid input.");
                wobble(this.textInput);
                return false;
            }
        }

        if (hasNumericValue && hasOperator) {
            return true;
        } else {
            setDebugLabel("Expression must contain at least one operator.");
            wobble(this.textInput);
            return false;
        }
    }

    private boolean isOperator(String token) {
        String[] operators = {"^", "+", "-", "*", "/", "(", ")"};
        return Arrays.asList(operators).contains(token);
    }


    public static boolean isNumeric(String s) {
        return s.matches("-?\\d+(\\.\\d+)?");
    }


    public void undo() {
        if (undoStack.size() > 1) {
            this.undoStack.pop();
            this.numberSet = undoStack.peek();
            setDebugLabel("Your set is now " + this.numberSet + ".");
            setDebugLabel(String.valueOf(undoStack));
            this.uiController.undoButtonColorDefault();
        } else {
            setDebugLabel("You can't undo any further.");
            this.uiController.undoButtonColorGray();
        }
        this.uiController.setSetLabel("Number Set: " + this.numberSet);
        this.textInput.requestFocus();
    }

    public int getTarget() {
        return this.targetNumber;
    }

    public LinkedList<Integer> getSet() {
        return this.numberSet;
    }

    public void wobble(TextField textField) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(50), this.textInput);
        tt.setFromX(-5);
        tt.setToX(5);
        tt.setCycleCount(4);
        tt.setAutoReverse(true);

        tt.play();

        // fade the background color from red to white over 1 second
        FadeTransition ft = new FadeTransition(Duration.seconds(1));
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.setOnFinished(e -> this.textInput.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null))));
        ft.play();
    }



    /**
     * DEBUG
     */

    private void createDebugPanel() {
        VBox debugPanel = new VBox();
        this.debugLabel = new javafx.scene.control.Label();
        debugPanel.getChildren().addAll(this.debugLabel);
        debugPanel.setAlignment(Pos.TOP_RIGHT);
        debugPanel.setPadding(new Insets(50,100,50,50));
        this.mathPane.getChildren().add(debugPanel);

    }


    public void setDebugLabel(String label) {
        this.debugLabel.setText(label);

        }


    }






