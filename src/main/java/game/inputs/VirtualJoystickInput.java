package game.inputs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Joystick virtual controlado pelo mouse.
 *
 * O mouse se move sobre um painel desenhado em Swing. A posição relativa
 * ao centro é traduzida em comandos (cima/baixo/esquerda/direita). Quando
 * o mouse fica na "zona morta" (perto do centro), retorna NONE.
 *
 * Repare que, para o Level, nao importa: recebia Keyboard, recebia Joystick
 * simulado com ThreadLocalRandom, agora recebe joystick simulado com mouse.
 * Uma única chamada em MainGame.java escolhe qual.
 */
public class VirtualJoystickInput implements IInputs {

    private volatile int mouseX = 100;
    private volatile int mouseY = 100;

    private final JFrame frame;
    private final JoystickPanel panel;

    public VirtualJoystickInput() {
        this.panel = new JoystickPanel();
        this.frame = new JFrame("Virtual Joystick");
        frame.add(panel);
        frame.pack();
        frame.setLocationByPlatform(true);

        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }

    @Override
    public Command waitCommand() {
        while (true) {
            Command cmd = getCurrentCommand();
            if (cmd != Command.NONE) {
                return cmd;
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return Command.EXIT;
            }
        }
    }

    private Command getCurrentCommand() {
        int dx = mouseX - panel.centerX;
        int dy = mouseY - panel.centerY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < panel.deadZone) {
            return Command.NONE;
        }

        if (Math.abs(dy) > Math.abs(dx)) {
            return dy < 0 ? Command.UP : Command.DOWN;
        } else {
            return dx < 0 ? Command.LEFT : Command.RIGHT;
        }
    }

    @Override
    public String getDeviceName() {
        return "Virtual Joystick (mouse)";
    }

    /** Painel que desenha o joystick virtual e rastreia o mouse. */
    private class JoystickPanel extends JPanel implements MouseMotionListener {

        final int centerX = 100;
        final int centerY = 100;
        final int deadZone = 20;

        JoystickPanel() {
            setPreferredSize(new Dimension(200, 200));
            setBackground(Color.BLACK);
            addMouseMotionListener(this);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(Color.DARK_GRAY);
            g.drawOval(centerX - 80, centerY - 80, 160, 160);

            g.setColor(Color.GRAY);
            g.drawOval(centerX - deadZone, centerY - deadZone, deadZone * 2, deadZone * 2);

            g.setColor(Color.YELLOW);
            g.fillOval(mouseX - 5, mouseY - 5, 10, 10);

            g.setColor(Color.WHITE);
            g.drawOval(centerX - 3, centerY - 3, 6, 6);
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            mouseX = e.getX();
            mouseY = e.getY();
            repaint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            mouseMoved(e);
        }
    }
}
