package game;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import game.inputs.ButtonInputs;
import game.inputs.IInputs;
import game.inputs.KeyboardInput;
import game.inputs.VirtualJoystickInput;
import game.ui.GUIRenderer;
import game.ui.IRenderer;
import game.ui.TerminalRenderer;
import game.utils.LevelLoader;
import game.world.Level;
import game.world.Progress;
import game.world.gobjects.Coin;
import game.world.gobjects.Player;

/**
 * Este e o unico lugar do sistema que decide QUAIS pecas concretas serao usadas.
 *
 * Repare: trocar de entrada e saida sao decisoes independentes, combinaveis
 * aqui em uma so linha. A classe Game, o Jogador, o Mapa e as Moedas nunca
 * mudam, e Game so enxerga IInputs e IRenderer.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        String mode = args.length > 0 ? args[0].trim().toLowerCase() : "terminal";
        int levelNumber = args.length > 1 ? Integer.parseInt(args[1]) : 1;
        List<Level> levels = new LevelLoader().loadAll(Path.of("levels.txt"));
        if (levelNumber < 1 || levelNumber > levels.size()) {
            throw new IllegalArgumentException("Level must be between 1 and " + levels.size() + ".");
        }

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

        Progress progress = new Progress();
        for (int index = levelNumber - 1; index < levels.size(); index++) {
            Level level = levels.get(index);
            Player player = new Player(level.getPlayerX(), level.getPlayerY());

            List<Coin> coins = new ArrayList<>();
            for (int[] position : level.getCoinPositions()) {
                coins.add(new Coin(position[0], position[1], 10));
            }

            Game game = new Game(level, player, progress, coins, input, renderer);
            if (!game.run()) {
                renderer.showMessage("Final score: " + progress.getScore());
                return;
            }
        }
        renderer.showMessage("FIM DE JOGO - Score: " + progress.getScore());
    }
}
