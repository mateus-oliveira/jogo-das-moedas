package game.enums;

import java.util.Locale;

public enum RendererEnum {
    GUI,
    TERMINAL;

    public static RendererEnum from() { return TERMINAL; }

    public static RendererEnum from(String mode) {
        try {
            return valueOf(mode.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                "Renderer must be gui or terminal: " + mode,
                exception
            );
        }
    }
}