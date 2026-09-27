package game.levels;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import game.world.Level;
import game.world.Map;

/** Responsavel por ler e converter levels.txt em dados de fase. */
public class LevelLoader {

    public Level load(Path file, int levelNumber) throws IOException {
        List<List<String>> levels = new ArrayList<>();
        List<String> currentLevel = new ArrayList<>();

        for (String line : Files.readAllLines(file)) {
            if (line.equals("---")) {
                levels.add(currentLevel);
                currentLevel = new ArrayList<>();
            } else if (!line.isBlank()) {
                currentLevel.add(line);
            }
        }
        if (!currentLevel.isEmpty()) {
            levels.add(currentLevel);
        }

        if (levelNumber < 1 || levelNumber > levels.size()) {
            throw new IllegalArgumentException("Level must be between 1 and " + levels.size() + ".");
        }

        List<String> rows = levels.get(levelNumber - 1);
        int width = rows.get(0).length();
        String[] mapRows = new String[rows.size()];
        int playerX = -1;
        int playerY = -1;
        List<Level.Position> coins = new ArrayList<>();

        for (int y = 0; y < rows.size(); y++) {
            if (rows.get(y).length() != width) {
                throw new IllegalArgumentException("Level " + levelNumber + " has rows with different widths.");
            }
            StringBuilder mapRow = new StringBuilder(width);
            for (int x = 0; x < width; x++) {
                char cell = rows.get(y).charAt(x);
                if (cell == '@') {
                    if (playerX >= 0) {
                        throw new IllegalArgumentException("Level " + levelNumber + " has more than one player.");
                    }
                    playerX = x;
                    playerY = y;
                    mapRow.append(' ');
                } else if (cell == '$') {
                    coins.add(new Level.Position(x, y));
                    mapRow.append(' ');
                } else if (cell == '#' || cell == '.') {
                    mapRow.append(cell == '#' ? '#' : ' ');
                } else {
                    throw new IllegalArgumentException("Invalid character in level " + levelNumber + ": " + cell);
                }
            }
            mapRows[y] = mapRow.toString();
        }

        if (playerX < 0 || coins.isEmpty()) {
            throw new IllegalArgumentException("Each level needs one player and at least one coin.");
        }
        return new Level(new Map(mapRows), playerX, playerY, coins);
    }
}