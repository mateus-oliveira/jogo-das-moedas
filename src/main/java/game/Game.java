package game;

import game.inputs.Command;
import game.inputs.IInputs;
import game.ui.IRenderer;
import game.world.Map;
import game.world.gobjects.Coin;
import game.world.gobjects.Player;
import java.util.List;

/**
 * VERSAO 2 - BAIXO ACOPLAMENTO / ALTA COESAO
 *
 * Responsabilidade unica desta classe: aplicar as REGRAS do jogo.
 * Quem move, quem colide, quem coleta, quem termina a partida.
 *
 * Tudo que nao e regra chega pronto pelo construtor - isso se chama
 * INJECAO DE DEPENDENCIA. A classe nao cria nada com "new": ela recebe.
 *
 * Consequencia pratica: Game nunca precisou saber que existe teclado.
 */
public class Game {

    private final Map map;
    private final Player player;
    private final List<Coin> coins;
    private final IInputs input;
    private final IRenderer renderer;

    private boolean running = true;

    public Game(
        Map map,
        Player player,
        List<Coin> coins,
        IInputs input,
        IRenderer renderer
    ) {
        this.map = map;
        this.player = player;
        this.coins = coins;
        this.input = input;
        this.renderer = renderer;
    }

    public void run() {
        renderer.clear();
        renderer.showMessage("=== Collect the coins ($) - control: " + input.getDeviceName() + " ===");
        renderer.draw(map, player, coins);

        while (running) {
            runTurn();
        }
        renderer.showMessage("Final score: " + player.getScore());
    }

    /**
     * Um unico turno do jogo. E publico de proposito: e esta a unidade
     * que os testes exercitam, sem precisar do laco nem de dispositivo nenhum.
     */
    public void runTurn() {
        Command command = input.waitCommand();

        if (command == Command.EXIT) {
            running = false;
            return;
        }
        if (command == Command.NONE) {
            renderer.showMessage("Unknown command.");
            return;
        }

        int destinationX = player.getX() + command.getDeltaX();
        int destinationY = player.getY() + command.getDeltaY();

        if (map.isWall(destinationX, destinationY)) {
            renderer.showMessage("Wall! You cannot go there.");
            return;
        }

        player.moveTo(destinationX, destinationY);
        collectCoinAtCurrentPosition();
        renderer.draw(map, player, coins);

        if (allCoinsCollected()) {
            renderer.showMessage("You collected all coins!");
            running = false;
        }
    }

    private void collectCoinAtCurrentPosition() {
        for (Coin coin : coins) {
            if (!coin.isCollected() && coin.isAt(player.getX(), player.getY())) {
                coin.collect();
                player.addScore(coin.getValue());
                renderer.showMessage("Coin collected! Score: " + player.getScore());
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
