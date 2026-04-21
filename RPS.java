import java.util.Random;
import java.util.Scanner;

public class RPS {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        RPSGame(scan);
    }

    static void RPSGame(Scanner scan) {
        System.out.println("Type Rock, Paper, or Scissors:");
        String userMove = scan.nextLine();

        Random gen = new Random();
        int compNum = gen.nextInt(3);

        String compMove;
        if (compNum == 0) {
            compMove = "Rock";
        } else if (compNum == 1) {
            compMove = "Paper";
        } else {
            compMove = "Scissors";
        }
        System.out.println("Computer chose " + compMove + "!");

        if (userMove.equals(compMove)) {
            System.out.println("It's a draw!");
        } else if (Winner(userMove, compMove)) {
            System.out.println("Player wins!");
        } else {
            System.out.println("Computer wins!");
        }
    }

    static boolean Winner(String userMove, String compMove) {
        if (userMove.equals("Rock")) {
            return compMove.equals("Scissors");
        } else if (userMove.equals("Paper")) {
            return compMove.equals("Rock");
        } else {
            return compMove.equals("Paper");
        }
    }
}