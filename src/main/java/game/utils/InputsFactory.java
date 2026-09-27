package game.utils;

import game.inputs.ButtonInputs;
import game.inputs.IInputs;
import game.inputs.KeyboardInput;
import game.inputs.VirtualJoystickInput;

public class InputsFactory {

	public static IInputs create(String mode) {
		return switch (mode) {
			case "gui" -> new ButtonInputs();
			case "joystick" -> new VirtualJoystickInput();
			default -> new KeyboardInput();
		};
	}
}
