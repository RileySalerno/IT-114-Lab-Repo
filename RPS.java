public class RPS{
    public static String getResult(int p1, int p2){
        if (p1 == p2) {
            System.out.println("Tie");
        } else if ( (p1 == 1 && p2 == 3) || (p1 == 2 && p2 == 1) || (p1 == 3 && p2 == 2)) {
            System.out.println("Player 1 wins");
        } 
        else {
            System.out.println("Player 2 wins");
        }

    }
}
