package game;

import java.util.List;
import java.util.Scanner;

/**
 * VERSAO 1 - ALTO ACOPLAMENTO / BAIXA COESAO
 *
 * Esta classe faz TUDO sozinha:
 *   1. guarda o mapa;
 *   2. guarda a posicao e a pontuacao do jogador;
 *   3. cria o Scanner e le o teclado;
 *   4. traduz a tecla digitada em movimento;
 *   5. verifica colisao com parede;
 *   6. controla a coleta de moedas;
 *   7. desenha o mapa na tela;
 *   8. controla o laco principal do jogo.
 *
 * Sao oito responsabilidades numa classe so. Repare que nao existe
 * NENHUM getter: nada aqui pode ser observado de fora.
 */
public class Game {

    private final List<char[][]> levels;
    private char[][] map;
    private int levelNumber;

    private int playerX;
    private int playerY;
    private int score = 0;
    private int remainingCoins;
    private boolean running = true;

    // ACOPLAMENTO: a classe Game decide sozinha que a entrada vem do teclado.
    // Para trocar por joystick ou toque na tela, alguem precisa editar ESTA linha.
    private final Scanner scanner = new Scanner(System.in);

    public Game() {
        this(1);
    }

    public Game(int levelNumber) {
        this.levels = new LevelLoader().loadAll();
        if (levelNumber < 1 || levelNumber > levels.size()) {
            throw new IllegalArgumentException("Level must be between 1 and " + levels.size() + ".");
        }
        this.levelNumber = levelNumber;
        loadLevel();
    }

    private void loadLevel() {
        map = levels.get(levelNumber - 1);
        playerX = 0;
        playerY = 0;
        remainingCoins = 0;
        for (int row = 0; row < map.length; row++) {
            for (int column = 0; column < map[row].length; column++) {
                if (map[row][column] == '@') {
                    playerX = column;
                    playerY = row;
                    map[row][column] = ' ';
                } else if (map[row][column] == '$') {
                    remainingCoins++;
                }
            }
        }
    }

    public void run() {
        System.out.println("=== Colete as moedas ($) ===");
        while (running) {
            while (running && remainingCoins > 0) {
                clear();
                draw();
                System.out.print("Comando (w/a/s/d, q para sair): ");

                if (!scanner.hasNextLine()) {
                    running = false;
                    break;
                }
                String command = scanner.nextLine().trim().toLowerCase();
                waitCommand(command);
            }

            if (!running) {
                break;
            }
            if (levelNumber == levels.size()) {
                System.out.println("FIM DE JOGO - Score: " + score);
                return;
            }

            levelNumber++;
            System.out.println("Nivel concluido. Iniciando nivel " + levelNumber + ".");
            loadLevel();
        }
        System.out.println("Final score: " + score);
    }

    private void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void waitCommand(String command) {
        int destinationX = playerX;
        int destinationY = playerY;

        switch (command) {
            case "w" -> destinationY -= 1;
            case "s" -> destinationY += 1;
            case "a" -> destinationX -= 1;
            case "d" -> destinationX += 1;
            case "q" -> {
                running = false;
                return;
            }
            default -> {
                System.out.println("Unknown command: " + command);
                return;
            }
        }

        if (map[destinationY][destinationX] == '#') {
            System.out.println("Wall! You cannot go there.");
            return;
        }

        playerX = destinationX;
        playerY = destinationY;

        if (map[playerY][playerX] == '$') {
            map[playerY][playerX] = ' ';
            score += 10;
            remainingCoins -= 1;
            System.out.println("Coin collected! Score: " + score);
        }

        if (remainingCoins == 0) {
            System.out.println("You collected all coins!");
        }
    }

    // Desenhar tambem esta aqui dentro, preso ao System.out.
    private void draw() {
        System.out.println();
        for (int row = 0; row < map.length; row++) {
            StringBuilder text = new StringBuilder();
            for (int column = 0; column < map[row].length; column++) {
                if (row == playerY && column == playerX) {
                    text.append('@');
                } else {
                    text.append(map[row][column]);
                }
            }
            System.out.println(text.toString());
        }
        System.out.println("Score: " + score + " | Remaining coins: " + remainingCoins);
    }
}
