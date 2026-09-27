package game.world;

import java.util.List;

/** Dados iniciais de uma fase, sem regras de carregamento ou execucao. */
public class Grid {

    private static final char WALL = '#';

    private final String[] rows;
    private final int playerX;
    private final int playerY;
    private final List<int[]> coinPositions;

    public Grid(String[] rows, int playerX, int playerY, List<int[]> coinPositions) {
        this.rows = rows.clone();
        this.playerX = playerX;
        this.playerY = playerY;
        this.coinPositions = List.copyOf(coinPositions);
    }

    public int getPlayerX() {
        return playerX;
    }

    public int getPlayerY() {
        return playerY;
    }

    public List<int[]> getCoinPositions() {
        return coinPositions;
    }

    public int getHeight() {
        return rows.length;
    }

    public int getWidth() {
        return rows[0].length();
    }

    /** Fora do nivel tambem conta como parede. */
    public boolean isWall(int x, int y) {
        if (y < 0 || y >= getHeight())
            return true;
        if (x < 0 || x >= rows[y].length())
            return true;
        return rows[y].charAt(x) == WALL;
    }
}