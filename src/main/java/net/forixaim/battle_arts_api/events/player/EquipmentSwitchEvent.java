package net.forixaim.battle_arts_api.events.player;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.entity.eventlistener.AbstractPlayerEvent;

public class EquipmentSwitchEvent<T extends PlayerPatch<?>> extends AbstractPlayerEvent<T> {
    private final ItemStack previousItem;
    private final ItemStack newItem;
    private final EquipmentSlot equipmentSlot;
    public EquipmentSwitchEvent(T playerPatch, boolean cancelable, ItemStack previousItem, ItemStack newItem, EquipmentSlot slot) {
        super(playerPatch, cancelable);
        this.previousItem = previousItem;
        this.newItem = newItem;
        this.equipmentSlot = slot;
    }

    public EquipmentSlot getSlot() {return equipmentSlot;}
    public ItemStack getPreviousItem() {
        return previousItem;
    }
    public ItemStack getNewItem() {
        return newItem;
    }
}
