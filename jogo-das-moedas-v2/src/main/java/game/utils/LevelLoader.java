package game.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import game.world.Grid;

/** Responsavel por ler e converter levels.txt em dados de fase. */
public class LevelLoader {

    public List<Grid> loadAll(Path file) throws IOException {
        List<List<String>> levels = new ArrayList<>();
        List<String> currentLevel = new ArrayList<>();

        for (String line : Files.readAllLines(file)) {
            if (line.equals("---")) {
                if (currentLevel.isEmpty()) {
                    throw new IllegalArgumentException("The level file contains an empty level.");
                }
                levels.add(currentLevel);
                currentLevel = new ArrayList<>();
            } else if (!line.isBlank()) {
                currentLevel.add(line);
            }
        }

        if (!currentLevel.isEmpty())
            levels.add(currentLevel);

        List<Grid> loadedLevels = new ArrayList<>();
        for (int index = 0; index < levels.size(); index++)
            loadedLevels.add(parseLevel(levels.get(index), index + 1));

        return loadedLevels;
    }

    public Grid load(Path file, int levelNumber) throws IOException {
        List<Grid> levels = loadAll(file);
        if (levelNumber < 1 || levelNumber > levels.size())
            throw new IllegalArgumentException(
                "Level must be between 1 and " + levels.size() + ".");
        return levels.get(levelNumber - 1);
    }

    private Grid parseLevel(List<String> rows, int levelNumber) {
        if (rows.isEmpty())
            throw new IllegalArgumentException(
                "Level " + levelNumber + " is empty.");
        int width = rows.get(0).length();
        String[] mapRows = new String[rows.size()];
        int playerX = -1;
        int playerY = -1;
        List<int[]> coins = new ArrayList<>();

        for (int y = 0; y < rows.size(); y++) {
            if (rows.get(y).length() != width)
                throw new IllegalArgumentException(
                    "Level " + levelNumber + " has rows with different widths."
                );

            StringBuilder mapRow = new StringBuilder(width);
            for (int x = 0; x < width; x++) {
                char cell = rows.get(y).charAt(x);
                switch (cell) {
                    case '@' -> {
                        if (playerX >= 0)
                            throw new IllegalArgumentException(
                                "Level " + levelNumber + " has more than one player."
                            );
                        playerX = x;
                        playerY = y;
                        mapRow.append(' ');
                    }
                    case '$' -> {
                        coins.add(new int[] { x, y });
                        mapRow.append(' ');
                    }
                    case '#', '.' -> mapRow.append(cell == '#' ? '#' : ' ');
                    default -> throw new IllegalArgumentException(
                        "Invalid character in level " + levelNumber + ": " + cell
                    );
                }
            }
            mapRows[y] = mapRow.toString();
        }

        if (playerX < 0 || coins.isEmpty()) {
            throw new IllegalArgumentException(
                "Each level needs one player and at least one coin.");
        }
        return new Grid(mapRows, playerX, playerY, coins);
    }
}