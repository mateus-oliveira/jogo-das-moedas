package game;

/**
 * Uma moeda parada no mapa, esperando ser coletada.
 *
 * DUPLICACAO: compare esta classe com Player, lado a lado. Os campos x e y,
 * o construtor, os getters e o isAt() sao exatamente os mesmos - copiados e
 * colados. Nada em comum foi declarado entre as duas.
 *
 * O compilador nao sabe que moeda e jogador sao ambos "coisas que ocupam uma
 * posicao no mapa", porque ninguem disse isso a ele. Consequencia pratica:
 * o ConsoleRenderer nao consegue tratar os dois do mesmo jeito, e teve que
 * escrever os simbolos '@' e '$' na mao.
 *
 * Na versao 2 isso vira a classe abstrata GameObject.
 */
public class Coin {

    private final int x;
    private final int y;
    private final int value;
    private boolean collected = false;

    public Coin(int x, int y, int value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getValue() {
        return value;
    }

    public boolean isAt(int otherX, int otherY) {
        return this.x == otherX && this.y == otherY;
    }

    public boolean isCollected() {
        return collected;
    }

    public void collect() {
        collected = true;
    }
}
