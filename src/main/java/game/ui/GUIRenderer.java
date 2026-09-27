package game.ui;

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

import game.world.Grid;
import game.world.Progress;
import game.world.gobjects.Coin;
import game.world.gobjects.Player;

/**
 * Desenha o jogo numa janela grafica (Swing), em vez do console.
 *
 * Repare: implementa o mesmo contrato de sempre, IRenderer. A classe Level
 * continua chamando clear(), showMessage() e draw() sem fazer ideia de que,
 * desta vez, existe uma janela do outro lado.
 *
 * Os metodos publicos so agendam trabalho na Event Dispatch Thread (via
 * invokeLater), porque componentes Swing so devem ser tocados por ela. Quem
 * chama estes metodos (a classe Level) continua rodando na thread principal,
 * sem nunca precisar saber disso.
 */
public class GUIRenderer extends JPanel implements IRenderer {

    private static final int CELL_SIZE = 80;

    private final JFrame frame;
    private final JLabel messageLabel;
    private Grid grid;
    private Player player;
    private Progress progress;
    private List<Coin> coins;

    public GUIRenderer() {
        this.messageLabel = new JLabel(" ");
        messageLabel.setFont(messageLabel.getFont().deriveFont(Font.BOLD, 14f));

        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(8 * CELL_SIZE, 6 * CELL_SIZE + 24));

        this.frame = new JFrame("Jogo das Moedas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(messageLabel, BorderLayout.SOUTH);
        SwingUtilities.invokeLater(() -> {
            frame.add(this, BorderLayout.CENTER);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
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
    public void draw(Grid grid, Player player, Progress progress, List<Coin> coins) {
        SwingUtilities.invokeLater(() -> {
            this.grid = grid;
            this.player = player;
            this.progress = progress;
            this.coins = coins;
            setPreferredSize(new Dimension(grid.getWidth() * CELL_SIZE, grid.getHeight() * CELL_SIZE + 24));
            revalidate();
            repaint();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (grid == null)
            return;

        g.setColor(Color.DARK_GRAY);
        for (int y = 0; y < grid.getHeight(); y++) {
            for (int x = 0; x < grid.getWidth(); x++) {
                if (grid.isWall(x, y))
                    g.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }

        g.setColor(Color.YELLOW);
        for (Coin coin : coins) {
            if (!coin.isCollected())
                g.fillOval(coin.getX() * CELL_SIZE + 8, coin.getY() * CELL_SIZE + 8, CELL_SIZE - 16, CELL_SIZE - 16);
        }

        g.setColor(Color.CYAN);
        g.fillRect(player.getX() * CELL_SIZE + 4, player.getY() * CELL_SIZE + 4, CELL_SIZE - 8, CELL_SIZE - 8);

        g.setColor(Color.WHITE);
        g.drawString("Score: " + progress.getScore(), 4, grid.getHeight() * CELL_SIZE + 18);
    }
}
