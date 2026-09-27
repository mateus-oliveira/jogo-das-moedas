package game.world;

import java.util.List;

/** Dados iniciais de uma fase, sem regras de carregamento ou execucao. */
public class Level {

    private final Map map;
    private final int playerX;
    private final int playerY;
    private final List<Position> coinPositions;

    public Level(Map map, int playerX, int playerY, List<Position> coinPositions) {
        this.map = map;
        this.playerX = playerX;
        this.playerY = playerY;
        this.coinPositions = List.copyOf(coinPositions);
    }

    public Map getMap() {
        return map;
    }

    public int getPlayerX() {
        return playerX;
    }

    public int getPlayerY() {
        return playerY;
    }

    public List<Position> getCoinPositions() {
        return coinPositions;
    }

    public static class Position {
        private final int x;
        private final int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}