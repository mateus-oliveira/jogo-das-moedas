package game.ui;

import java.util.List;

import game.world.Level;
import game.world.Progress;
import game.world.gobjects.Coin;
import game.world.gobjects.Player;

/**
 * O CONTRATO DA SAIDA.
 *
 * Mesma ideia da IInputs, do outro lado: o jogo sabe QUE precisa
 * desenhar, mas nao sabe ONDE. Console hoje, janela grafica amanha,
 * e nos testes um renderizador de mentira que so anota o que aconteceu.
 */
public interface IRenderer {

    void clear();

    void showMessage(String message);

    void draw(Level level, Player player, Progress progress, List<Coin> coins);

}
