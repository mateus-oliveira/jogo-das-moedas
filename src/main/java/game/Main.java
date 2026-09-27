package game;

import game.inputs.*;
import game.ui.*;
import game.world.Map;
import game.world.gobjects.Coin;
import game.world.gobjects.Player;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Map map = Map.standard();
        Player player = new Player(1, 1);

        List<Coin> coins = new ArrayList<Coin>();
        coins.add(new Coin(6, 2, 10));
        coins.add(new Coin(4, 4, 10));

        IInputs input = new KeyboardInput();
        IRenderer renderer = new TerminalRenderer();

        Game game = new Game(map, player, coins, input, renderer);
        game.run();
    }
}
