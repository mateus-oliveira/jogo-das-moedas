package game.utils;

import game.ui.GUIRenderer;
import game.ui.IRenderer;
import game.ui.TerminalRenderer;

public class RendererFactory {

	public static IRenderer create(String mode) {
		return switch (mode) {
			case "gui", "joystick" -> new GUIRenderer();
			default -> new TerminalRenderer();
		};
	}
}
