package game.world.gobjects;

/** Uma moeda parada no mapa, esperando ser coletada. */
public class Coin extends GameObject {

    private final int value;
    private boolean collected = false;

    public Coin(int x, int y, int value) {
        super(x, y);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public boolean isCollected() {
        return collected;
    }

    public void collect() {
        collected = true;
    }

    @Override
    public char getSymbol() {
        return '$';
    }
}
