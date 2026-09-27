package game;

/**
 * O jogador.
 *
 * A primeira vista parece uma classe bem resolvida: guarda x, y e sabe se
 * mover. Coesa, pequena, sem System.out.
 *
 * Mas olhe a assinatura de canMoveTo(). Para responder "posso andar para
 * la?", o jogador precisa receber o MAPA INTEIRO, no formato char[][], e
 * precisa conhecer o caractere que representa parede.
 *
 * ACOPLAMENTO: Player depende da REPRESENTACAO que o LevelLoader escolheu.
 * Troque char[][] por String[] e esta classe para de compilar - mesmo que
 * a ideia de "jogador" nao tenha mudado em nada.
 */
public class Player {

    private int x;
    private int y;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isAt(int otherX, int otherY) {
        return this.x == otherX && this.y == otherY;
    }

    public void moveTo(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    /** ACOPLAMENTO: o jogador nao deveria precisar conhecer char[][] nem '#'. */
    public boolean canMoveTo(char[][] map, int destinationX, int destinationY) {
        if (destinationY < 0 || destinationY >= map.length)
            return false;
        if (destinationX < 0 || destinationX >= map[destinationY].length)
            return false;
        return map[destinationY][destinationX] != '#';
    }
}
