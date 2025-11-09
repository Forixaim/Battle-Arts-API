package net.forixaim.battle_arts_api.client.input.action;

import net.forixaim.battle_arts_api.client.KeyBinds;
import net.minecraft.client.KeyMapping;
import org.jetbrains.annotations.NotNull;
import yesman.epicfight.api.client.input.action.InputAction;

public enum BattleArtsInputActions implements InputAction
{
    COMBAT_ART(KeyBinds.USE_ART_1),
    MANA_ART(KeyBinds.USE_MANA_ART),
    BURST_ART(KeyBinds.USE_BURST_ART),
    ULTIMATE_ART(KeyBinds.USE_ULTIMATE_ART);

    final int id;
    final KeyMapping keyMapping;
    BattleArtsInputActions(KeyMapping keyMapping)
    {
        this.keyMapping = keyMapping;
        this.id = InputAction.ENUM_MANAGER.assign(this);
    }

    @Override
    public @NotNull KeyMapping keyMapping() {
        return keyMapping;
    }

    @Override
    public int universalOrdinal() {
        return id;
    }
}
