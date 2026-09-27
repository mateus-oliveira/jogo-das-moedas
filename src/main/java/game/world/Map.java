package game.world;

/**
 * O cenario do jogo.
 *
 * Responsabilidade unica: dizer o que e parede e o que e chao.
 */
public class Map {

    private static final char WALL = '#';

    private final String[] rows;

    public Map(String[] rows) {
        this.rows = rows;
    }

    /** Mapa padrao usado nas aulas. */
    public static Map standard() {
        return new Map(new String[] {
            "########",
            "#   #  #",
            "# #    #",
            "# ###  #",
            "#      #",
            "########"
        });
    }

    public int getHeight() {
        return rows.length;
    }

    public int getWidth() {
        return rows[0].length();
    }

    /** Fora do mapa tambem conta como parede. */
    public boolean isWall(int x, int y) {
        if (y < 0 || y >= getHeight()) {
            return true;
        }
        if (x < 0 || x >= rows[y].length()) {
            return true;
        }
        return rows[y].charAt(x) == WALL;
    }
}
