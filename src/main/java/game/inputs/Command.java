package game.inputs;

/**
 * A LINGUAGEM COMUM DO JOGO.
 *
 * O jogo nao entende "tecla W", "eixo Y do analogico" nem "swipe para cima".
 * Ele entende CIMA. Cada fonte de entrada e responsavel por traduzir o que
 * conhece para um destes comandos.
 */
public enum Command {

    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0),
    EXIT(0, 0),
    NONE(0, 0);

    private final int deltaX;
    private final int deltaY;

    Command(int deltaX, int deltaY) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }

    public int getDeltaX() {
        return deltaX;
    }

    public int getDeltaY() {
        return deltaY;
    }
}
