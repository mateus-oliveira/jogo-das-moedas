package game;

import game.inputs.IInputs;
import game.inputs.JoystickInput;
import game.inputs.KeyboardInput;
import game.inputs.MobileInput;
import game.ui.TerminalRenderer;
import game.world.Map;
import game.world.gobjects.Coin;
import game.world.gobjects.Player;
import java.util.ArrayList;
import java.util.List;

/**
 * Este e o unico lugar do sistema que decide QUAIS pecas concretas serao usadas.
 *
 * Repare: trocar teclado por joystick e uma linha aqui. A classe Game,
 * o Jogador, o Mapa e as Moedas nao mudam nem um caractere.
 */
public class Main {

    public static void main(String[] args) {
        Map map = Map.standard();
        Player player = new Player(1, 1);

        List<Coin> coins = new ArrayList<>();
        coins.add(new Coin(6, 2, 10));
        coins.add(new Coin(4, 4, 10));

        IInputs input = chooseInput(args);

        Game game = new Game(map, player, coins, input, new TerminalRenderer());
        game.run();
    }

    private static IInputs chooseInput(String[] args) {
        String choice = "keyboard";
        if (args.length > 0) {
            choice = args[0].trim().toLowerCase();
        }

        if (choice.equals("joystick")) {
            return new JoystickInput();
        }else if (choice.equals("mobile")) {
            return new MobileInput();
        }
        return new KeyboardInput();
    }
}
