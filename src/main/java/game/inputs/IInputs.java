package game.inputs;

import game.enums.CommandsEnum;

/**
 * O CONTRATO DA ENTRADA.
 *
 * Esta interface e a resposta para a pergunta: "e se amanha o jogo tiver
 * joystick ou controle por toque?". A classe Level depende DESTE contrato,
 * nunca de um dispositivo especifico.
 *
 * Por que interface e nao classe abstrata? Porque nao existe nenhum codigo
 * para compartilhar entre teclado, joystick e toque - cada um le de um lugar
 * completamente diferente. Nao ha nada a herdar, so um contrato a cumprir.
 */
public interface IInputs {

    /** Devolve o proximo comando do jogador, ja traduzido. */
    CommandsEnum waitCommand();

    /** Nome do dispositivo, apenas para exibir na tela. */
    String getDeviceName();

}
