package game.world.gobjects;

/**
 * Tudo que ocupa uma posicao no mapa.
 *
 * Aqui uma CLASSE ABSTRATA faz sentido (diferente da entrada, que virou
 * interface): jogador e moeda compartilham estado real - as coordenadas -
 * e o codigo que cuida delas. So o simbolo desenhado muda, e por isso ele
 * e o unico metodo abstrato.
 */
public abstract class GameObject {

    private int x;
    private int y;

    protected GameObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    protected void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isAt(int x, int y) {
        return this.x == x && this.y == y;
    }

    /** Cada gameObject decide como é desenhado. */
    public abstract char getSymbol();
}
