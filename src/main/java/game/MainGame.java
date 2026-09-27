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
import game.world.Grid;
import game.world.Progress;
import game.world.gobjects.Coin;
import game.world.gobjects.Player;

/**
 * Este e o unico lugar do sistema que decide QUAIS pecas concretas serao usadas.
 *
 * Repare: trocar de entrada e saida sao decisoes independentes, combinaveis
 * aqui em uma so linha. A classe Level, o Jogador, o Grid e as Moedas nunca
 * mudam, e Level so enxerga IInputs e IRenderer.
 */
public class MainGame {

    public static void main(String[] args) throws IOException {
        String mode = args.length > 0 ? args[0].trim().toLowerCase() : "terminal";
        int levelNumber = args.length > 1 ? Integer.parseInt(args[1]) : 1;
        List<Grid> grids = new LevelLoader().loadAll(Path.of("levels.txt"));
        if (levelNumber < 1 || levelNumber > grids.size()) {
            throw new IllegalArgumentException("Level must be between 1 and " + grids.size() + ".");
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
        for (int index = levelNumber - 1; index < grids.size(); index++) {
            Grid grid = grids.get(index);
            Player player = new Player(grid.getPlayerX(), grid.getPlayerY());

            List<Coin> coins = new ArrayList<>();
            for (int[] position : grid.getCoinPositions()) {
                coins.add(new Coin(position[0], position[1], 10));
            }

            Level level = new Level(grid, player, progress, coins, input, renderer);
            if (!level.run()) {
                renderer.showMessage("Final score: " + progress.getScore());
                return;
            }
        }
        renderer.showMessage("FIM DE JOGO - Score: " + progress.getScore());
    }
}
