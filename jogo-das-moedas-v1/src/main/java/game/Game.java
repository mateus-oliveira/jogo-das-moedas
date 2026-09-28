package game;

import java.util.ArrayList;
import java.util.List;

/**
 * VERSAO 1 - ALTO ACOPLAMENTO
 *
 * Esta versao e o degrau do meio da aula. Comparada com a v0, ela parece um
 * progresso: o jogador virou Player, a moeda virou Coin, a leitura do teclado
 * virou KeyboardInput, o desenho virou ConsoleRenderer. Cinco classes onde
 * antes havia uma.
 *
 * E ainda assim quase nada melhorou. Porque o que foi feito foi QUEBRAR em
 * classes, nao INVERTER as dependencias. Game continua decidindo tudo:
 *
 *   - decide que existe teclado (new KeyboardInput)
 *   - decide que existe joystick (new JoystickInput)
 *   - decide que a saida e o console (new ConsoleRenderer, e impressao solta)
 *   - decide como cada dispositivo fala (o switch de waitCommand)
 *
 * A prova esta no campo useJoystick, logo abaixo. Ele existe porque o jogo
 * ganhou uma SEGUNDA entrada, e a unica forma de encaixa-la sem redesenhar
 * nada foi espalhar "if (useJoystick)" pela classe. Sao QUATRO pontos, todos
 * marcados com o comentario CICATRIZ, todos dentro da classe de REGRAS - que
 * nao tem nada a ver com dispositivo de entrada.
 *
 * A pergunta da aula nao e "isso funciona?". Funciona. A pergunta e: quanto
 * custa a TERCEIRA entrada?
 */
public class Game {

    private final List<char[][]> levels;
    private char[][] grid;
    private int levelNumber;

    private Player player;
    private List<Coin> coins;

    private int score = 0;

    /**
     * Estado redundante: o tamanho de coins e a quantidade de moedas nao
     * coletadas ja dizem isso. Duas fontes de verdade para o mesmo fato,
     * e duas chances de esquecer de atualizar uma delas.
     */
    private int remainingCoins;

    private boolean running = true;

    /**
     * ACOPLAMENTO: a classe Game decide sozinha de onde vem a entrada.
     * Para trocar de dispositivo, alguem precisa editar ESTA classe.
     *
     * E um boolean, e um boolean so sabe dizer "sim" ou "nao". Com dois
     * dispositivos ele da conta. Com tres, nao existe terceiro valor.
     */
    private final boolean useJoystick;

    /** Um destes dois campos e SEMPRE null. O compilador nao reclama. */
    private KeyboardInput keyboard;
    private JoystickInput joystick;

    /** ACOPLAMENTO: a classe das regras escolhendo a tecnologia de saida. */
    private final ConsoleRenderer renderer = new ConsoleRenderer();

    public Game() { this(false, 1); }

    public Game(boolean useJoystick, int levelNumber) {
        this.levels = new LevelLoader().loadAll();
        if (levelNumber < 1 || levelNumber > levels.size()) {
            throw new IllegalArgumentException(
                "Level must be between 1 and " + levels.size() + ".");
        }
        this.levelNumber = levelNumber;
        this.useJoystick = useJoystick;

        // ===== CICATRIZ 1 de 4: escolher e CRIAR o dispositivo =====
        if (useJoystick) {
            this.joystick = new JoystickInput();
        } else {
            this.keyboard = new KeyboardInput();
        }

        loadLevel();
    }

    /**
     * ACOPLAMENTO: este metodo apaga '@' e '$' de dentro de grid - e grid e o
     * MESMO array que esta guardado na lista levels, nao uma copia. O grid
     * original foi destruido ao ser carregado.
     *
     * Na pratica o jogo nunca volta para uma fase ja jogada, entao o bug nao
     * aparece. Mas ele esta aqui: reinicie a fase 1 e voce vai encontrar um
     * grid sem jogador e sem moedas. Isso e o preco de passar estado mutavel
     * cru de uma classe para outra.
     */
    private void loadLevel() {
        grid = levels.get(levelNumber - 1);
        coins = new ArrayList<>();
        int startX = 0;
        int startY = 0;

        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid[row].length; column++) {
                if (grid[row][column] == '@') {
                    startX = column;
                    startY = row;
                    grid[row][column] = ' ';
                } else if (grid[row][column] == '$') {
                    coins.add(new Coin(column, row, 10));
                    grid[row][column] = ' ';
                }
            }
        }

        player = new Player(startX, startY);
        remainingCoins = coins.size();
    }

    public void run() {
        System.out.println("=== Colete as moedas ($) ===");
        while (running) {
            while (running && remainingCoins > 0) {
                clear();
                renderer.draw(grid, player, coins, score, remainingCoins, levelNumber, deviceName());
                waitCommand(readCommand());
            }

            if (!running)
                break;

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

    // ===== CICATRIZ 2 de 4: rotear a leitura para o dispositivo certo =====
    private String readCommand() {
        if (useJoystick)
            return joystick.readCommand();
        return keyboard.readCommand();
    }

    // ===== CICATRIZ 3 de 4: descobrir o nome do dispositivo para a tela =====
    private String deviceName() {
        return useJoystick ? "Joystick simulado" : "Teclado";
    }

    /** ACOPLAMENTO: codigo ANSI de terminal escrito dentro da classe de regras. */
    private void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * ===== CICATRIZ 4 de 4: entender o que cada dispositivo fala =====
     *
     * Esta e a cicatriz mais cara das quatro. O teclado manda "w", o joystick
     * manda "up", e as duas coisas querem dizer "para cima".
     *
     * Conte os literais de texto abaixo: sao DEZ, para cinco conceitos. Cada
     * dispositivo novo com vocabulario proprio soma mais cinco. E tudo isso
     * mora na classe das REGRAS, que nao deveria saber o que e uma tecla.
     */
    private void waitCommand(String command) {
        int destinationX = player.getX();
        int destinationY = player.getY();

        switch (command) {
            case "w", "up" -> destinationY -= 1;
            case "s", "down" -> destinationY += 1;
            case "a", "left" -> destinationX -= 1;
            case "d", "right" -> destinationX += 1;
            case "q", "quit" -> {
                running = false;
                return;
            }
            default -> {
                System.out.println("Unknown command: " + command);
                return;
            }
        }

        if (!player.canMoveTo(grid, destinationX, destinationY)) {
            System.out.println("Wall! You cannot go there.");
            return;
        }

        player.moveTo(destinationX, destinationY);
        collectCoinAtCurrentPosition();

        if (remainingCoins == 0)
            System.out.println("You collected all coins!");
    }

    private void collectCoinAtCurrentPosition() {
        for (Coin coin : coins) {
            if (!coin.isCollected() && coin.isAt(player.getX(), player.getY())) {
                coin.collect();
                score += coin.getValue();
                remainingCoins -= 1;
                System.out.println("Coin collected! Score: " + score);
            }
        }
    }
}
