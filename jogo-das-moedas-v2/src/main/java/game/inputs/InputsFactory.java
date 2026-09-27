package game.inputs;

import game.enums.InputsEnum;
import game.utils.IFactory;

public class InputsFactory implements IFactory<IInputs, InputsEnum> {

    @Override
    public IInputs create(InputsEnum mode) {
        return switch (mode) {
            case BUTTONS -> new ButtonInputs();
            case JOYSTICK -> new VirtualJoystickInput();
            case KEYBOARD -> new KeyboardInput();
        };
    }
}