package game.ui;

import game.enums.InputsEnum;
import game.utils.IFactory;

public class RendererFactory implements IFactory<IRenderer, InputsEnum> {

    @Override
    public IRenderer create(InputsEnum key) {
        return switch (key) {
            case BUTTONS, JOYSTICK -> new GUIRenderer();
            case KEYBOARD -> new TerminalRenderer();
        };
    }
}