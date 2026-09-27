package game;

import java.util.List;

import game.enums.CommandsEnum;
import game.inputs.IInputs;
import game.ui.IRenderer;
import game.world.Grid;
import game.world.Progress;
import game.world.gobjects.Coin;
import game.world.gobjects.Player;

/**
 * VERSAO 2 - BAIXO ACOPLAMENTO / ALTA COESAO
 *
 * Responsabilidade unica desta classe: aplicar as REGRAS do jogo.
 * Quem move, quem colide, quem coleta, quem termina a partida.
 *
 * Tudo que nao e regra chega pronto pelo construtor - isso se chama
 * INJECAO DE DEPENDENCIA. A classe nao cria nada com "new": ela recebe.
 *
 * Consequencia pratica: Level nunca precisou saber que existe teclado.
 */
public class Level {

    private final Grid grid;
    private final Player player;
    private final Progress progress;
    private final List<Coin> coins;
    private final IInputs input;
    private final IRenderer renderer;

    private boolean running = true;

    public Level(
        Grid grid,
        Player player,
        Progress progress,
        List<Coin> coins,
        IInputs input,
        IRenderer renderer
    ) {
        this.grid = grid;
        this.player = player;
        this.progress = progress;
        this.coins = coins;
        this.input = input;
        this.renderer = renderer;
    }

    public boolean run() {
        renderer.clear();
        renderer.showMessage("=== Collect the coins ($) - control: " + input.getDeviceName() + " ===");
        renderer.draw(grid, player, progress, coins);

        while (running) {
            runTurn();
        }
        return allCoinsCollected();
    }

    /**
     * Um unico turno do jogo. E publico de proposito: e esta a unidade
     * que os testes exercitam, sem precisar do laco nem de dispositivo nenhum.
     */
    public void runTurn() {
        CommandsEnum command = input.waitCommand();

        if (command == CommandsEnum.EXIT) {
            running = false;
            return;
        }
        if (command == CommandsEnum.NONE) {
            renderer.showMessage("Unknown command.");
            return;
        }

        int destinationX = player.getX() + command.getDeltaX();
        int destinationY = player.getY() + command.getDeltaY();

        if (grid.isWall(destinationX, destinationY)) {
            renderer.showMessage("Wall! You cannot go there.");
            return;
        }

        player.moveTo(destinationX, destinationY);
        collectCoinAtCurrentPosition();
        renderer.draw(grid, player, progress, coins);

        if (allCoinsCollected()) {
            renderer.showMessage("You collected all coins!");
            running = false;
        }
    }

    private void collectCoinAtCurrentPosition() {
        for (Coin coin : coins) {
            if (!coin.isCollected() && coin.isAt(player.getX(), player.getY())) {
                coin.collect();
                progress.addScore(coin.getValue());
                renderer.showMessage("Coin collected! Score: " + progress.getScore());
            }
        }
    }

    private boolean allCoinsCollected() {
        for (Coin coin : coins) {
            if (!coin.isCollected())
                return false;
        }
        return true;
    }
}
