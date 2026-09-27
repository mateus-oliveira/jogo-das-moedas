package game.enums;

/** Comandos que a fase entende, independentes do dispositivo de entrada. */
public enum CommandsEnum {

    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0),
    EXIT(0, 0),
    NONE(0, 0);

    private final int deltaX;
    private final int deltaY;

    CommandsEnum(int deltaX, int deltaY) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }

    public int getDeltaX() {
        return deltaX;
    }

    public int getDeltaY() {
        return deltaY;
    }
}