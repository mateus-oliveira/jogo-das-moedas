package game;

import game.inputs.ButtonInputs;
import game.inputs.IInputs;
import game.inputs.KeyboardInput;
import game.inputs.VirtualJoystickInput;
import game.ui.GUIRenderer;
import game.ui.IRenderer;
import game.ui.TerminalRenderer;
import game.world.Map;
import game.world.gobjects.Coin;
import game.world.gobjects.Player;
import java.util.ArrayList;
import java.util.List;

/**
 * Este e o unico lugar do sistema que decide QUAIS pecas concretas serao usadas.
 *
 * Repare: trocar de entrada e saida sao decisoes independentes, combinaveis
 * aqui em uma so linha. A classe Game, o Jogador, o Mapa e as Moedas nunca
 * mudam, e Game so enxerga IInputs e IRenderer.
 */
public class Main {

    public static void main(String[] args) {
        Map map = Map.standard();
        Player player = new Player(1, 1);

        List<Coin> coins = new ArrayList<>();
        coins.add(new Coin(6, 2, 10));
        coins.add(new Coin(4, 4, 10));

        String mode = args.length > 0 ? args[0].trim().toLowerCase() : "terminal";

        IInputs input;
        IRenderer renderer;

        switch (mode) {
            case "gui" -> {
                input = new ButtonInputs();
                renderer = new GUIRenderer();
            }
            case "joystick" -> {
                input = new VirtualJoystickInput();
                renderer = new GUIRenderer();
            }
            default -> {
                input = new KeyboardInput();
                renderer = new TerminalRenderer();
            }
        }

        Game game = new Game(map, player, coins, input, renderer);
        game.run();
    }
}
