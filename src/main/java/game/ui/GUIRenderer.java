package game.ui;

import game.world.Map;
import game.world.gobjects.Coin;
import game.world.gobjects.Player;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Desenha o jogo numa janela grafica (Swing), em vez do console.
 *
 * Repare: implementa o mesmo contrato de sempre, IRenderer. A classe Game
 * continua chamando clear(), showMessage() e draw() sem fazer ideia de que,
 * desta vez, existe uma janela do outro lado.
 *
 * Os metodos publicos so agendam trabalho na Event Dispatch Thread (via
 * invokeLater), porque componentes Swing so devem ser tocados por ela. Quem
 * chama estes metodos (a classe Game) continua rodando na thread principal,
 * sem nunca precisar saber disso.
 */
public class GUIRenderer implements IRenderer {

    private static final int CELL_SIZE = 32;

    private final JFrame frame;
    private final GridPanel gridPanel;
    private final JLabel messageLabel;

    public GUIRenderer() {
        this.gridPanel = new GridPanel();
        this.messageLabel = new JLabel(" ");
        messageLabel.setFont(messageLabel.getFont().deriveFont(Font.BOLD, 14f));

        this.frame = new JFrame("Jogo das Moedas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(gridPanel, BorderLayout.CENTER);
        frame.add(messageLabel, BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);

        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }

    @Override
    public void clear() {
        SwingUtilities.invokeLater(() -> messageLabel.setText(" "));
    }

    @Override
    public void showMessage(String message) {
        SwingUtilities.invokeLater(() -> messageLabel.setText(message));
    }

    @Override
    public void draw(Map map, Player player, List<Coin> coins) {
        SwingUtilities.invokeLater(() -> gridPanel.updateState(map, player, coins));
    }

    /** Painel responsavel apenas por pintar o estado atual do jogo. */
    private static class GridPanel extends JPanel {

        private Map map;
        private Player player;
        private List<Coin> coins;

        GridPanel() {
            setBackground(Color.BLACK);
            setPreferredSize(new Dimension(8 * CELL_SIZE, 6 * CELL_SIZE + 24));
        }

        void updateState(Map map, Player player, List<Coin> coins) {
            this.map = map;
            this.player = player;
            this.coins = coins;
            setPreferredSize(new Dimension(map.getWidth() * CELL_SIZE, map.getHeight() * CELL_SIZE + 24));
            revalidate();
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (map == null) {
                return;
            }

            g.setColor(Color.DARK_GRAY);
            for (int y = 0; y < map.getHeight(); y++) {
                for (int x = 0; x < map.getWidth(); x++) {
                    if (map.isWall(x, y)) {
                        g.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    }
                }
            }

            g.setColor(Color.YELLOW);
            for (Coin coin : coins) {
                if (!coin.isCollected()) {
                    g.fillOval(coin.getX() * CELL_SIZE + 8, coin.getY() * CELL_SIZE + 8, CELL_SIZE - 16, CELL_SIZE - 16);
                }
            }

            g.setColor(Color.CYAN);
            g.fillRect(player.getX() * CELL_SIZE + 4, player.getY() * CELL_SIZE + 4, CELL_SIZE - 8, CELL_SIZE - 8);

            g.setColor(Color.WHITE);
            g.drawString("Score: " + player.getScore(), 4, map.getHeight() * CELL_SIZE + 18);
        }
    }
}
