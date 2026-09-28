package game.ui;

import game.enums.RendererEnum;
import game.utils.IFactory;

public class RendererFactory implements IFactory<IRenderer, RendererEnum> {

    @Override
    public IRenderer create(RendererEnum key) {
        return switch (key) {
            case GUI -> new GUIRenderer();
            case TERMINAL -> new TerminalRenderer();
        };
    }
}