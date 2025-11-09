package net.forixaim.battle_arts_api.client.input.handlers;

import com.mojang.blaze3d.platform.InputConstants;
import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.battle_arts_api.client.input.InputMode;
import net.forixaim.battle_arts_api.client.input.PlayerInputState;
import net.forixaim.battle_arts_api.client.input.action.BattleArtsInputActions;
import net.forixaim.battle_arts_api.client.input.controller.IBattleArtsControllerMod;
import net.forixaim.battle_arts_api.mixin.ControlEngineInvoker;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.InputEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlot;

import java.util.function.Consumer;


@ApiStatus.Experimental
public final class InputManager {
    private InputManager() {
    }

    public static boolean supportsControllerInput() {
        final IBattleArtsControllerMod controllerMod = SharedMethods.getAPI();
        if (controllerMod == null) {
            return false;
        }
        return controllerMod.getInputMode().supportsController();
    }

    public static boolean isActionActive(@NotNull BattleArtsInputActions action) {
        final IBattleArtsControllerMod controllerMod = SharedMethods.getAPI();
        if (controllerMod == null) {
            return isKeyDown(action.keyMapping());
        }

        return switch (controllerMod.getInputMode()) {
            case KEYBOARD_MOUSE -> isKeyDown(action.keyMapping());
            case CONTROLLER -> controllerMod.getBinding(action).isDigitalActiveNow();
            case MIXED -> isKeyDown(action.keyMapping()) || controllerMod.getBinding(action).isDigitalActiveNow();
        };
    }

    public static void triggerOnPress(@NotNull BattleArtsInputActions action, boolean interactionKeyEventCheck, @NotNull DiscreteActionHandler handler) {
        DiscreteInputActionTrigger.triggerOnPress(action, (context) -> {
            if (context.triggeredByController() || !interactionKeyEventCheck) {
                handler.onAction(context);
                return;
            }

            runKeyboardMouseEvent(action, handler);
        });
    }

    public static void castActiveSkill(BattleArtsInputActions action, boolean interactionKeyEventCheck, SkillSlot skillSlotConsumer, LocalPlayerPatch localPlayerPatch) {
        Runnable castActiveSkill = () -> {
            SkillContainer activeSlot = localPlayerPatch.getSkill(skillSlotConsumer);
            if (activeSlot.sendCastRequest(localPlayerPatch, ClientEngine.getInstance().controlEngine).shouldReserveKey())
            {
                ((ControlEngineInvoker)ClientEngine.getInstance().controlEngine).invokeReserveKey(activeSlot.getSlot(), BattleArtsInputActions.COMBAT_ART.keyMapping());
            }
        };
        triggerOnPress(action, interactionKeyEventCheck, castActiveSkill);
    }


    public static void triggerOnPress(@NotNull BattleArtsInputActions action, boolean interactionKeyEventCheck, @NotNull Runnable runnable) {
        triggerOnPress(action, interactionKeyEventCheck, (context) -> runnable.run());
    }

    public static boolean isBoundToSamePhysicalInput(@NotNull BattleArtsInputActions action, @NotNull BattleArtsInputActions action2) {
        final IBattleArtsControllerMod controllerMod = SharedMethods.getAPI();
        if (controllerMod != null && controllerMod.getInputMode() == InputMode.CONTROLLER) {
            return controllerMod.isBoundToSameButton(action, action2);
        }

        final KeyMapping keyMapping1 = action.keyMapping();
        final KeyMapping keyMapping2 = action2.keyMapping();
        return keyMapping1.getKey() == keyMapping2.getKey();
    }


    @NotNull
    public static PlayerInputState getInputState(@NotNull Input vanillaInput) {
        final IBattleArtsControllerMod controllerMod = SharedMethods.getAPI();
        if (controllerMod != null && controllerMod.getInputMode() == InputMode.CONTROLLER) {
            return controllerMod.getInputState();
        }

        return PlayerInputState.fromVanillaInput(vanillaInput);
    }


    @NotNull
    public static PlayerInputState getInputState(@NotNull LocalPlayer localPlayer) {
        return getInputState(localPlayer.input);
    }

    public static void setInputState(@NotNull PlayerInputState inputState) {
        final LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            Input input = player.input;
            PlayerInputState.applyToVanillaInput(inputState, input);
        }
    }


    @SuppressWarnings("JavadocReference")
    private static void runKeyboardMouseEvent(@NotNull BattleArtsInputActions action, @NotNull DiscreteActionHandler handler) {
        final KeyMapping keyMapping = action.keyMapping();

        final InputConstants.Key key = keyMapping.getKey();
        final boolean isMouse = InputConstants.Type.MOUSE == key.getType();

        final int mouseButton = isMouse ? key.getValue() : -1;

        @SuppressWarnings("UnstableApiUsage")
        InputEvent.InteractionKeyMappingTriggered inputEvent = ForgeHooksClient.onClickInput(
                mouseButton, keyMapping, InteractionHand.MAIN_HAND
        );

        if (!inputEvent.isCanceled()) {
            handler.onAction(new DiscreteActionHandler.Context(false));
        }
    }

    private static boolean isKeyDown(@NotNull KeyMapping keyMapping) {
        if (keyMapping.isDown()) {
            return true;
        }
        final InputConstants.Key key = keyMapping.getKey();
        final int keyValue = key.getValue();
        final long windowPointer = Minecraft.getInstance().getWindow().getWindow();

        if (key.getType() == InputConstants.Type.KEYSYM) {
            return GLFW.glfwGetKey(windowPointer, keyValue) > 0;
        } else if (key.getType() == InputConstants.Type.MOUSE) {
            return GLFW.glfwGetMouseButton(windowPointer, keyValue) > 0;
        }
        return false;
    }
}
