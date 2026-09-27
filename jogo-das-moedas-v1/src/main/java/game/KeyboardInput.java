package game;

import java.util.Scanner;

/**
 * Le comandos digitados no console.
 *
 * ACOPLAMENTO 1 - devolve String CRUA: "w", "a", "s", "d", "q". Quem chamar
 * precisa saber traduzir essas letras em movimento. Essa traducao acabou
 * dentro da classe Game, que e a classe das REGRAS do jogo.
 *
 * ACOPLAMENTO 2 - uma classe de ENTRADA imprimindo na SAIDA. O prompt esta
 * aqui porque nao havia outro lugar obvio para coloca-lo. Repare que o texto
 * do prompt cita "w/a/s/d": este arquivo e o Game sabem a MESMA coisa sobre
 * as teclas, cada um do seu jeito.
 */
public class KeyboardInput {

    private final Scanner scanner = new Scanner(System.in);

    public String readCommand() {
        System.out.print("Comando (w/a/s/d, q para sair): ");
        if (!scanner.hasNextLine())
            return "q";
        return scanner.nextLine().trim().toLowerCase();
    }
}
