public class RPS {
    private Player p1;
    private Player p2;

    public RPS(Player p1,Player p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public void gameStart() throws Exception {
        p1.send("Matched with: ");
        p2.send("Matched with: ");

        int move1 = p1.getMove();
        int move2 = p2.getMove();

        String result = getResult(move1, move2);

        p1.send(result);
        p2.send(result);
    }

    private String getResult(int p1, int p2) {
        if (p1 == p2) return "Tie";

        if ((p1 == 1 && p2 == 3) || (p1 == 2 && p2 == 1) || (p1 == 3 && p2 == 2)) {
            return "Player 1 wins";
        }
        return "Player 2 wins";
    }
}
