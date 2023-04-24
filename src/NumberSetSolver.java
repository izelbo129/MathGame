import org.mariuszgromada.math.mxparser.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class NumberSetSolver {

    private final LinkedList<Integer> numberSet;
    private final int targetNumber;

    public NumberSetSolver(LinkedList<Integer> numberSet, int targetNumber) {
        this.numberSet = numberSet;
        this.targetNumber = targetNumber;
        System.out.println("Target number: " + targetNumber);
        int closestDifference = solve();
        System.out.println("Closest difference: " + closestDifference);
    }

    public int solve() {
        List<String> expressions = generateExpressions();
        int closestDifference = Integer.MAX_VALUE;
        for (String expression : expressions) {
            Expression e = new Expression(expression);
            double result = e.calculate();
            int intResult = (int) result;
            if (result == intResult) {
                result = intResult;
            }
            int difference = (int) Math.abs(result - targetNumber);
            if (difference < closestDifference) {
                closestDifference = difference;
            }
        }
        return closestDifference;
    }

    private List<String> generateExpressions() {
        List<String> expressions = new ArrayList<>();
        generateExpressionsHelper(expressions, new Stack<String>(), 0);
        return expressions;
    }

    private void generateExpressionsHelper(List<String> expressions, Stack<String> stack, int index) {
        if (index == numberSet.size()) {
            while (stack.size() > 1) {
                String right = stack.pop();
                String operator = stack.pop();
                String left = stack.pop();
                stack.push(left + operator + right);
            }
            expressions.add(stack.pop());
        } else {
            int num = numberSet.get(index);
            stack.push(String.valueOf(num));
            generateExpressionsHelper(expressions, stack, index + 1);
            stack.pop();

            if (!stack.empty()) {
                String right = String.valueOf(num);
                String operator = stack.pop();
                String left = stack.pop();
                stack.push("(" + left + operator + right + ")");
                generateExpressionsHelper(expressions, stack, index + 1);
                stack.pop();
                if (!stack.empty()) {
                    stack.push(left);
                    stack.push(operator);
                }
            }
            stack.push("+");
            generateExpressionsHelper(expressions, stack, index + 1);
            stack.pop();
            stack.push("-");
            generateExpressionsHelper(expressions, stack, index + 1);
            stack.pop();
            stack.push("*");
            generateExpressionsHelper(expressions, stack, index + 1);
            stack.pop();
            stack.push("/");
            generateExpressionsHelper(expressions, stack, index + 1);
            stack.pop();
            stack.push("^");
            generateExpressionsHelper(expressions, stack, index + 1);
            stack.pop();
        }
    }

}