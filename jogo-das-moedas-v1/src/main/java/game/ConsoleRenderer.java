package game;

import java.util.List;

/**
 * Desenha o jogo no console.
 *
 * Alguem extraiu esta classe de dentro de Game com a melhor das intencoes, e
 * parou no meio do caminho. O resultado ensina mais que o erro original.
 *
 * ACOPLAMENTO 1 - conte os parametros de draw(): sete. Sempre que algo novo
 * precisar aparecer na tela (vidas, tempo, nome da fase), muda ESTA
 * assinatura E a chamada dentro de Game. Dois arquivos, toda vez.
 *
 * ACOPLAMENTO 2 - os simbolos '@' e '$' estao escritos aqui de novo, embora
 * Player e Coin ja existam. Se a moeda virar '*', ha dois lugares para
 * lembrar. Como Player e Coin nao tem nada em comum declarado, nao havia
 * como perguntar a eles.
 *
 * ACOPLAMENTO 3 - recebe char[][]. Este e o terceiro arquivo preso a
 * representacao escolhida pelo LevelLoader (os outros sao Game e Player).
 *
 * E o mais importante: extrair esta classe NAO desacoplou nada. Game
 * continua dando "new ConsoleRenderer()" e continua imprimindo direto com
 * System.out em outros dez lugares. Extrair uma classe nao e o mesmo que
 * inverter uma dependencia.
 */
public class ConsoleRenderer {

    public void draw(
        char[][] map,
        Player player,
        List<Coin> coins,
        int score,
        int remainingCoins,
        int levelNumber,
        String deviceName
    ) {
        System.out.println();
        System.out.println("Nivel " + levelNumber + " - controle: " + deviceName);
        for (int row = 0; row < map.length; row++) {
            StringBuilder text = new StringBuilder();
            for (int column = 0; column < map[row].length; column++) {
                text.append(symbolAt(map, player, coins, column, row));
            }
            System.out.println(text.toString());
        }
        System.out.println("Score: " + score + " | Remaining coins: " + remainingCoins);
    }

    private char symbolAt(char[][] map, Player player, List<Coin> coins, int x, int y) {
        if (player.isAt(x, y))
            return '@';
        for (Coin coin : coins) {
            if (!coin.isCollected() && coin.isAt(x, y))
                return '$';
        }
        return map[y][x];
    }
}
