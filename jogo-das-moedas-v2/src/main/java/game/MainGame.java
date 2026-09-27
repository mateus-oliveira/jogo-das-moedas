package game;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import game.enums.InputsEnum;
import game.inputs.IInputs;
import game.inputs.InputsFactory;
import game.ui.IRenderer;
import game.ui.RendererFactory;
import game.utils.LevelLoader;
import game.world.Grid;
import game.world.Progress;
import game.world.gobjects.Coin;
import game.world.gobjects.Player;

/**
 * Este e o unico lugar do sistema que decide QUAIS pecas concretas serao usadas.
 *
 * Compare com a versao 1: la, a escolha do dispositivo estava espalhada por
 * dentro da classe de regras. Aqui ela cabe em duas linhas, e a classe Level,
 * o Player, o Grid e as Coins nunca mudam - Level so enxerga IInputs e
 * IRenderer.
 *
 * Uma ressalva honesta: hoje um unico InputsEnum escolhe o PAR entrada+saida,
 * porque RendererFactory recebe o mesmo enum que InputsFactory. Entrada e
 * saida PODERIAM ser escolhidas separadamente - e o desacoplamento e
 * justamente o que torna isso possivel - mas este MainGame ainda nao faz
 * isso. Separar os dois seletores e um exercicio da aula, e repare que ele
 * nao exige tocar em Level, Player, Coin nem Grid.
 */
public class MainGame {

    public static void main(String[] args) throws IOException {
        int levelNumber = args.length > 1 ? Integer.parseInt(args[1]) : 1;
        List<Grid> grids = new LevelLoader().loadAll(Path.of("levels.txt"));
        InputsEnum mode = args.length > 0
            ? InputsEnum.from(args[0])
            : InputsEnum.from();

        if (levelNumber < 1 || levelNumber > grids.size())
            throw new IllegalArgumentException(
                "Level must be between 1 and " + grids.size() + ".");

        IInputs input = new InputsFactory().create(mode);
        IRenderer renderer = new RendererFactory().create(mode);

        Progress progress = new Progress();

        for (int index = levelNumber - 1; index < grids.size(); index++) {
            Grid grid = grids.get(index);
            Player player = new Player(grid.getPlayerX(), grid.getPlayerY());

            List<Coin> coins = new ArrayList<>();
            for (int[] position : grid.getCoinPositions())
                coins.add(new Coin(position[0], position[1], 10));

            Level level = new Level(grid, player, progress, coins, input, renderer);
            if (!level.run()) {
                renderer.showMessage("Final score: " + progress.getScore());
                return;
            }
        }
        renderer.showMessage("FIM DE JOGO - Score: " + progress.getScore());
    }
}
