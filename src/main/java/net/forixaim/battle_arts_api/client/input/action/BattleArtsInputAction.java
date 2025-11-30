package net.forixaim.battle_arts_api.client.input.action;

import net.forixaim.battle_arts_api.client.KeyBinds;
import net.minecraft.client.KeyMapping;
import org.jetbrains.annotations.NotNull;
import yesman.epicfight.api.client.input.action.InputAction;
import yesman.epicfight.api.client.input.controller.ControllerBinding;

import java.util.Optional;

public enum BattleArtsInputAction implements InputAction {
    COMBAT_ART(KeyBinds.USE_ART_1),
    MANA_ART(KeyBinds.USE_MANA_ART),
    BURST_ART(KeyBinds.USE_BURST_ART),
    ULTIMATE_ART(KeyBinds.USE_ULTIMATE_ART);

    final int id;
    final KeyMapping keyMapping;

    BattleArtsInputAction(KeyMapping keyMapping) {
        this.keyMapping = keyMapping;
        this.id = InputAction.ENUM_MANAGER.assign(this);
    }

    @Override
    public @NotNull KeyMapping keyMapping() {
        return keyMapping;
    }

    @Override
    public @NotNull Optional<@NotNull ControllerBinding> controllerBinding() {
        // TODO: Add controller support for these input bindings
        //  https://github.com/Forixaim/Battle-Arts-API/issues/2
        return Optional.empty();
    }

    @Override
    public boolean isVanilla() {
        return false;
    }

    @Override
    public int universalOrdinal() {
        return id;
    }
}
