package game.world;

/** Progresso cumulativo do jogador entre missoes. */
public class Progress {

    private int score;

    public void addScore(int amount) {
        score += amount;
    }

    public int getScore() {
        return score;
    }
}