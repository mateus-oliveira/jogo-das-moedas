package game.inputs;

import java.util.Scanner;

import game.enums.CommandsEnum;

/** Traduz teclas digitadas no console em comandos do jogo. */
public class KeyboardInput implements IInputs {

    private final Scanner scanner;

    public KeyboardInput() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public CommandsEnum waitCommand() {
        System.out.print("Comando (w/a/s/d, q para sair): ");
        if (!scanner.hasNextLine())
            return CommandsEnum.EXIT;
        String key = scanner.nextLine().trim().toLowerCase();

        return switch (key) {
            case "w" -> CommandsEnum.UP;
            case "s" -> CommandsEnum.DOWN;
            case "a" -> CommandsEnum.LEFT;
            case "d" -> CommandsEnum.RIGHT;
            case "q" -> CommandsEnum.EXIT;
            default -> CommandsEnum.NONE;
        };
    }

    @Override
    public String getDeviceName() {
        return "Keyboard";
    }
}
