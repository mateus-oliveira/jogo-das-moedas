package game;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Joystick simulado: sorteia uma direcao a cada turno.
 *
 * Esta foi a SEGUNDA entrada do jogo, adicionada depois que tudo ja estava
 * escrito. Ela e o motivo pelo qual este projeto existe na aula.
 *
 * ACOPLAMENTO - repare no vocabulario: este dispositivo devolve "up", "down",
 * "left" e "right". NAO devolve "w", "a", "s", "d". Sao duas linguagens
 * diferentes para dizer exatamente a mesma coisa, e nenhuma das duas esta
 * escrita em lugar nenhum como um tipo - sao Strings soltas.
 *
 * Consequencia: a classe Game teve que aprender AS DUAS linguagens. Veja o
 * switch de Game.waitCommand(): ele carrega 10 literais de texto, quando 5
 * conceitos (cima, baixo, esquerda, direita, sair) bastariam. A terceira
 * entrada vai levar esse numero para 15.
 */
public class JoystickInput {

    private static final String[] DIRECTIONS = { "up", "down", "left", "right" };

    public String readCommand() {
        String direction = DIRECTIONS[ThreadLocalRandom.current().nextInt(DIRECTIONS.length)];
        System.out.println("Joystick -> " + direction);
        pause();
        return direction;
    }

    private void pause() {
        try {
            Thread.sleep(120);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }
    }
}
