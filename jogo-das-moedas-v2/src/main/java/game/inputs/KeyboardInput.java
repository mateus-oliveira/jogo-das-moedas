package game.inputs;

import java.util.Scanner;

import game.enums.CommandsEnum;

/**
 * Traduz teclas digitadas no console em comandos do jogo.
 *
 * Repare no que esta classe NAO faz: ela nao imprime nada. Quem escreve o
 * prompt na tela e o TerminalRenderer, porque prompt e SAIDA. Uma classe de
 * entrada que imprime seria o mesmo vazamento que a versao 1 tem.
 */
public class KeyboardInput implements IInputs {

    private final Scanner scanner;

    public KeyboardInput() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public CommandsEnum waitCommand() {
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
