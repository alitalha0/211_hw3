package penguingame.main;
import penguingame.game.GameController;

public class SlidingPuzzleApp {
    public static void main(String[] args) {
        // initializes a game controller then starts the game, startGame() method determines the flow of entire game.
        GameController controller = new GameController();
        controller.startGame();
    }
}
