package game.ui;

import game.world.Map;
import game.world.Progress;
import game.world.gobjects.Coin;
import game.world.gobjects.Player;
import java.util.List;

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

    void draw(Map map, Player player, Progress progress, List<Coin> coins);

}
