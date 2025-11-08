package net.forixaim.battle_arts_api.client.input.controller;

import net.forixaim.battle_arts_api.client.input.InputMode;
import net.forixaim.battle_arts_api.client.input.PlayerInputState;
import net.forixaim.battle_arts_api.client.input.action.BattleArtsInputActions;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;


/**
 * Represents an integration layer for third-party controller mods used by Epic Fight.
 * <p>
 * This interface must be implemented by any external controller mod to provide
 * controller input support for Epic Fight. It acts as a bridge between the
 * controller mod’s input system (e.g., Controlify, Controllable, MidnightControls) and Epic Fight’s
 * input handling logic.
 * <p>
 * Epic Fight relies on this interface to determine and manage the current
 * {@link InputMode}. Since input mode management is not part of the vanilla
 * Minecraft input system, controller mods must supply their own implementation.
 * <p>
 * <b>Note:</b> This interface exposes low-level controller integration. Most consumers should
 * use a higher-level abstraction unless direct access is necessary for functionality
 * that cannot be achieved otherwise.
 * <p>
 * <b>Warning:</b> This API is currently marked as experimental.
 * This designation does not imply that the implementation is of an 'experimental' quality,
 * but rather indicates that classes, methods, and fields may be subject to renaming, relocation, or removal.
 * The Epic Fight team reserves the right to modify or completely remove any components of the API at any time,
 * without prior notice.
 * <p>
 */
@ApiStatus.Experimental
public interface IBattleArtsControllerMod {

    String getModName();


    @NotNull
    InputMode getInputMode();


    @NotNull
    ControllerBinding getBinding(BattleArtsInputActions action);

    @NotNull
    PlayerInputState getInputState();

    boolean isBoundToSameButton(@NotNull BattleArtsInputActions action, @NotNull BattleArtsInputActions action2);
}
