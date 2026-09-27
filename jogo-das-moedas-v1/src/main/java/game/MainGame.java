package game;

public class MainGame {
    public static void main(String[] args) {
        int levelNumber = args.length > 0 ? Integer.parseInt(args[0]) : 1;
        Game game = new Game(levelNumber);
        game.run();
    }
}
