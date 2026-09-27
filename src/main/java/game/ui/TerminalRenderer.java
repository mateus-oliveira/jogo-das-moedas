package game.ui;

import java.util.List;

import game.world.Grid;
import game.world.Progress;
import game.world.gobjects.Coin;
import game.world.gobjects.Player;

/** Draws the game in text mode. */
public class TerminalRenderer implements IRenderer {

    @Override
    public void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    @Override
    public void draw(Grid grid, Player player, Progress progress, List<Coin> coins) {
        System.out.println();
        for (int y = 0; y < grid.getHeight(); y++) {
            StringBuilder line = new StringBuilder();
            for (int x = 0; x < grid.getWidth(); x++) {
                line.append(symbolAt(grid, player, coins, x, y));
            }
            System.out.println(line.toString());
        }
        System.out.println("Score: " + progress.getScore());
    }

    private char symbolAt(Grid grid, Player player, List<Coin> coins, int x, int y) {
        if (player.isAt(x, y))
            return player.getSymbol();
        for (Coin coin : coins) {
            if (!coin.isCollected() && coin.isAt(x, y)) {
                return coin.getSymbol();
            }
        }
        if (grid.isWall(x, y))
            return '#';
        return ' ';
    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }
}
