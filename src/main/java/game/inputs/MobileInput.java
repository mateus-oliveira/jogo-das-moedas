package game.inputs;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Traduz gestos de tela (swipe) em comandos do jogo.
 *
 * Num aplicativo real, os gestos viriam do sistema operacional.
 * Aqui eles chegam prontos pelo construtor.
 */
public class MobileInput implements IInputs {

    private static final String[] GESTURES = {
        "swipe-cima",
        "swipe-baixo",
        "swipe-esquerda",
        "swipe-direita",
        "toque-duplo"
    };

    public MobileInput() {}

    @Override
    public Command waitCommand() {

        // ThreadLocalRandom simula uma entrada real do touch screen.
        String gesture = GESTURES[
            ThreadLocalRandom.current().nextInt(GESTURES.length)
        ];

        return switch (gesture) {
            case "swipe-cima" -> Command.UP;
            case "swipe-baixo" -> Command.DOWN;
            case "swipe-esquerda" -> Command.LEFT;
            case "swipe-direita" -> Command.RIGHT;
            case "toque-duplo" -> Command.EXIT;
            default -> Command.NONE;
        };
    }

    @Override
    public String getDeviceName() {
        return "Mobile";
    }
}
