import java.util.Scanner;

public class RPS{
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.print("Player 1 - Enter (1) for Rock, (2) for Paper, or (3) for Scissors");
        int p1 = scan.nextInt();

        System.out.print("Player 2 - Enter (1) for Rock, (2) for Paper, or (3) for Scissors");
        int p2 = scan.nextInt();

        if (p1 < 1 || p1 > 3 || p2 < 1 || p2 > 3) {
            System.out.println("Please enter 1, 2, or 3");
        } else if (p1 == p2) {
            System.out.println("Tie");
        } else if ( (p1 == 1 && p2 == 3) || (p1 == 2 && p2 == 1) || (p1 == 3 && p2 == 2)) {
            System.out.println("Player 1 wins");
        } 
        else {
            System.out.println("Player 2 wins");
        }

        scan.close();
    }
}
