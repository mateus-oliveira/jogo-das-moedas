package game.enums;

import java.util.Locale;

public enum InputsEnum {
    BUTTONS,
    JOYSTICK,
    KEYBOARD;

    public static InputsEnum from() { return KEYBOARD; }
    public static InputsEnum from(String mode) {
        try {
            return valueOf(mode.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                "Mode must be buttons, joystick, or keyboard: " + mode,
                exception
            );
        }
    }
}