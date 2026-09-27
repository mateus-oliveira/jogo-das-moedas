package game;

public class MainGame {

    public static void main(String[] args) {
        // ACOPLAMENTO: sem enum e sem fabrica, o modo de entrada vira um
        // boolean decidido por uma cadeia de if/else sobre texto solto.
        //
        // Este e o quinto ponto de edicao que a segunda entrada custou. E
        // repare: para adicionar a terceira, este if/else nao serve mais.
        // Um boolean nao tem terceiro valor - seria preciso trocar o tipo,
        // e com ele a assinatura do construtor de Game.
        boolean useJoystick;
        if (args.length == 0 || args[0].equalsIgnoreCase("keyboard")) {
            useJoystick = false;
        } else if (args[0].equalsIgnoreCase("joystick")) {
            useJoystick = true;
        } else {
            throw new IllegalArgumentException(
                "Mode must be keyboard or joystick: " + args[0]);
        }

        int levelNumber = args.length > 1 ? Integer.parseInt(args[1]) : 1;

        Game game = new Game(useJoystick, levelNumber);
        game.run();
    }
}
