import java.util.LinkedList;
import java.util.Scanner;
import org.mariuszgromada.math.mxparser.*;

import static java.lang.Math.abs;

public class MathController {

    private int targetNumber;
    private LinkedList numberSet;


    public MathController() {
        License.iConfirmNonCommercialUse("IanZelbo");
        this.numberSet = new LinkedList();
        this.targetNumber = (int) (Math.random() * 101);
        for (int i = 0; i < 5; i++) {
            int setRandom = (int) (Math.random() * 6);
            this.numberSet.add(setRandom);
        }

        System.out.println("Your target number is " + this.targetNumber);
        System.out.println("Your number set is " + this.numberSet);
        System.out.println("Try to get as close as possible. Good luck!");
        calculateSet();

    }

    public void calculateSet() {


        Scanner scanner = new Scanner(System.in);
        String expression = scanner.nextLine();
        Expression e = new Expression(expression);
        double targetDifference = abs(this.targetNumber - e.calculate());

        System.out.println("Your answer " + e.calculate() + " was " + targetDifference + " away from the Target Number.");

    }
}
