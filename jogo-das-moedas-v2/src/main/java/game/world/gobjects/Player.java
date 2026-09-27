package game.world.gobjects;

/**
 * O jogador.
 *
 * Responsabilidade unica: saber onde esta e como se movimentar.
 * Repare no que esta classe NAO faz: nao le teclado, nao desenha nada,
 * nao conhece o mapa e nao sabe se existe joystick no mundo.
 */
public class Player extends GameObject {

    public Player(int x, int y) {
        super(x, y);
    }

    public void moveTo(int newX, int newY) {
        setPosition(newX, newY);
    }

    @Override
    public char getSymbol() {
        return '@';
    }
}
