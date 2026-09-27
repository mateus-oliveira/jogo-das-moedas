package game.inputs;

import java.util.Scanner;

/** Traduz teclas digitadas no console em comandos do jogo. */
public class KeyboardInput implements IInputs {

    private final Scanner scanner;

    public KeyboardInput() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public Command waitCommand() {
        System.out.print("Comando (w/a/s/d, q para sair): ");
        if (!scanner.hasNextLine())
            return Command.EXIT;
        String key = scanner.nextLine().trim().toLowerCase();

        return switch (key) {
            case "w" -> Command.UP;
            case "s" -> Command.DOWN;
            case "a" -> Command.LEFT;
            case "d" -> Command.RIGHT;
            case "q" -> Command.EXIT;
            default -> Command.NONE;
        };
    }

    @Override
    public String getDeviceName() {
        return "Keyboard";
    }
}
