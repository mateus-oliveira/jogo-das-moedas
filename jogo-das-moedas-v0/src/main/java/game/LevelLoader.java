package game;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/** Le os mapas separados por --- no arquivo levels.txt. */
public class LevelLoader {

    public List<char[][]> loadAll() {
        try {
            List<String> lines = Files.readAllLines(Path.of("levels.txt"));
            List<List<String>> levels = new ArrayList<>();
            List<String> currentLevel = new ArrayList<>();

            for (String line : lines) {
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
            if (!currentLevel.isEmpty()) {
                levels.add(currentLevel);
            }

            List<char[][]> maps = new ArrayList<>();
            for (int index = 0; index < levels.size(); index++) {
                maps.add(parseLevel(levels.get(index), index + 1));
            }
            return maps;
        } catch (IOException exception) {
            throw new IllegalStateException("Could not read levels.txt from the current directory.", exception);
        }
    }

    public char[][] load(int levelNumber) {
        List<char[][]> levels = loadAll();
        if (levelNumber < 1 || levelNumber > levels.size()) {
            throw new IllegalArgumentException("Level must be between 1 and " + levels.size() + ".");
        }
        return levels.get(levelNumber - 1);
    }

    private char[][] parseLevel(List<String> rows, int levelNumber) {
        if (rows.isEmpty()) {
            throw new IllegalArgumentException("Level " + levelNumber + " is empty.");
        }
        int width = rows.get(0).length();
        char[][] map = new char[rows.size()][width];
        for (int row = 0; row < rows.size(); row++) {
            if (rows.get(row).length() != width) {
                throw new IllegalArgumentException("Level " + levelNumber + " has rows with different widths.");
            }
            for (int column = 0; column < width; column++) {
                char cell = rows.get(row).charAt(column);
                map[row][column] = cell == '.' ? ' ' : cell;
            }
        }
        return map;
    }
}