import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

import org.mariuszgromada.math.mxparser.*;

import static java.lang.Math.abs;

public class MathController {

    private final int targetNumber;
    private LinkedList<Integer> numberSet;

    private Stack<LinkedList<Integer>> undoStack;


    public MathController() {
        this.undoStack = new Stack<>();
        License.iConfirmNonCommercialUse("IanZelbo");
        this.numberSet = new LinkedList<>();
        this.targetNumber = (int) (Math.random() * 101);
        for (int i = 0; i < 5; i++) {
            int setRandom = (int) (Math.random() * 6);
            this.numberSet.add(setRandom);
        }
        this.undoStack.push(new LinkedList<>(this.numberSet));

        System.out.println("Your target number is " + this.targetNumber);
        System.out.println("Your number set is " + this.numberSet);
        System.out.println("Try to get as close as possible. Good luck! You can use 'undo' and 'quit'.");
        callInput();

    }

    private void callInput() {
        Scanner scanner = new Scanner(System.in);
        String input;

        while (true) {
            input = scanner.nextLine();

            if (input.equalsIgnoreCase("undo")) {
                undo();
            }
            else if (input.equalsIgnoreCase("quit")) {
                    System.out.println("Thanks for playing!");
                    System.exit(0);
            } else {
                Expression e = new Expression(input);
                if (validateExpression(input)) {
                    calculateSet(input, e);
                }
            }

        }
    }




    public void calculateSet(String expression, Expression e) {
        expression = expression.replaceAll("\\s+", "");
        String[] tokens = expression.split("(?<=\\^)|(?=\\^)|(?<=\\+)|(?=\\+)|(?<=-)|(?=-)|(?<=\\*)|(?=\\*)|(?<=/)|(?=/)|(?<=()|(?=()|(?<=)|(?=)))"); // Split expression into tokens

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
            System.out.println("Your new set is " + this.numberSet);
            callInput();
        } else {
            int targetDifference = abs((int) (this.targetNumber - e.calculate()));
            System.out.println("Your number " + e.calculate() + " is " + targetDifference + " away from the target number " + this.targetNumber + ".");
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
                    System.out.println("Invalid input.");
                    return false;
                }
                hasNumericValue = true;
                lastTokenIsNumeric = true;
            } else if (isOperator(token)) {
                hasOperator = true;
                if ("/".equals(token)) {
                    if (lastTokenIsNumeric && "0".equals(tokens[tokens.length - 1])) {
                        System.out.println("Invalid input.");
                        return false;
                    }
                }
                lastTokenIsNumeric = false;
            } else {
                System.out.println("Invalid input.");
                return false;
            }
        }

        if (hasNumericValue && hasOperator) {
            return true;
        } else {
            System.out.println("Expression must contain at least one operator.");
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
        if (undoStack.size()>1) {
            this.undoStack.pop();
            this.numberSet = undoStack.peek();
            System.out.println("Your set is now " + this.numberSet + ".");
        } else {
            System.out.println("You can't undo any further.");
        }
    }
}




