package game.inputs;

import java.awt.GridLayout;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

/**
 * Traduz cliques em botoes de uma janela grafica em comandos do jogo.
 *
 * O jogo pede o proximo comando (waitCommand) rodando na thread principal;
 * os botoes sao clicados na Event Dispatch Thread do Swing. A fila
 * bloqueante e o ponto de encontro seguro entre as duas threads: o clique
 * poe um comando na fila, e waitCommand() so acorda quando ha um la dentro.
 *
 * Repare que esta classe nao conhece o GUIRenderer, nem o Level. Ela so
 * cumpre o contrato IInputs - exatamente como o KeyboardInput.
 */
public class ButtonInputs implements IInputs {

    private final BlockingQueue<Command> commands = new LinkedBlockingQueue<>();
    private final JFrame frame;

    public ButtonInputs() {
        this.frame = new JFrame("Controles");
        frame.setLayout(new GridLayout(3, 3, 4, 4));

        frame.add(new JLabel());
        frame.add(button("Cima", Command.UP));
        frame.add(new JLabel());

        frame.add(button("Esquerda", Command.LEFT));
        frame.add(button("Sair", Command.EXIT));
        frame.add(button("Direita", Command.RIGHT));

        frame.add(new JLabel());
        frame.add(button("Baixo", Command.DOWN));
        frame.add(new JLabel());

        frame.pack();
        frame.setLocationByPlatform(true);

        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }

    private JButton button(String label, Command command) {
        JButton button = new JButton(label);
        button.addActionListener(event -> commands.offer(command));
        return button;
    }

    @Override
    public Command waitCommand() {
        try {
            return commands.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Command.EXIT;
        }
    }

    @Override
    public String getDeviceName() {
        return "Button GUI";
    }
}
