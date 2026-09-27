package game.inputs;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Traduz a leitura dos eixos de um analogico em comandos do jogo.
 *
 * Num jogo real, os valores dos eixos viriam do driver do controle.
 * Aqui eles chegam prontos pelo construtor, so para a aula: o que importa
 * e que a TRADUCAO acontece aqui dentro, e nao na classe Game.
 */
public class JoystickInput implements IInputs {

    private static final double DEAD_ZONE = 0.5;
    private double x, y;

    public JoystickInput() {}

    @Override
    public Command waitCommand() {

        // ThreadLocalRandom simula uma entrada real entre -1 e 1.
        x = ThreadLocalRandom.current().nextDouble(-1.0, 1.0);
        y = ThreadLocalRandom.current().nextDouble(-1.0, 1.0);

        if (x > DEAD_ZONE) {
            return Command.RIGHT;
        } else if (x < -DEAD_ZONE) {
            return Command.LEFT;
        } else if (y > DEAD_ZONE) {
            return Command.DOWN;
        } else if (y < -DEAD_ZONE) {
            return Command.UP;
        } else {
            return Command.NONE;
        }
    }

    @Override
    public String getDeviceName() {
        return "Joystick";
    }
}
