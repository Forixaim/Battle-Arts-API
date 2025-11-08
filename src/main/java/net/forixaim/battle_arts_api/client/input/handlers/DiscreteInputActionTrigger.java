package net.forixaim.battle_arts_api.client.input.handlers;

import net.forixaim.battle_arts_api.client.input.action.BattleArtsInputActions;
import net.forixaim.battle_arts_api.client.input.controller.BattleArtsControllerModProvider;
import net.forixaim.battle_arts_api.client.input.controller.ControllerBinding;
import net.forixaim.battle_arts_api.client.input.controller.IBattleArtsControllerMod;
import net.minecraft.client.KeyMapping;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;



@ApiStatus.Internal
public final class DiscreteInputActionTrigger {
    private DiscreteInputActionTrigger() {
    }

    @Nullable
    private static IBattleArtsControllerMod getControllerModApi() {
        return BattleArtsControllerModProvider.get();
    }

    public static void triggerOnPress(BattleArtsInputActions action, DiscreteActionHandler handler) {
        final IBattleArtsControllerMod controllerMod = getControllerModApi();
        final KeyMapping keyMapping = action.keyMapping();
        if (controllerMod == null) {
            handleKeyboardAndMouse(keyMapping, handler);
            return;
        }

        switch (controllerMod.getInputMode()) {
            case MIXED -> {
                final boolean handled = handleController(controllerMod.getBinding(action), handler);
                if (handled) {
                    return;
                }
                handleKeyboardAndMouse(keyMapping, handler);
            }
            case CONTROLLER -> handleController(controllerMod.getBinding(action), handler);
            case KEYBOARD_MOUSE -> handleKeyboardAndMouse(keyMapping, handler);
        }
    }

    private static void handleKeyboardAndMouse(KeyMapping keyMapping, DiscreteActionHandler handler) {
        while (keyMapping.consumeClick()) {
            handler.onAction(createContext(false));
        }
    }

    private static boolean handleController(ControllerBinding controllerBinding, DiscreteActionHandler handler) {
        if (controllerBinding.isDigitalJustPressed()) {
            handler.onAction(createContext(true));
            return true;
        }
        return false;
    }
    
    @NotNull
    private static DiscreteActionHandler.Context createContext(boolean triggeredByController) {
        return new DiscreteActionHandler.Context(triggeredByController);
    }
}
