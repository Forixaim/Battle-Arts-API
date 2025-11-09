package net.forixaim.battle_arts_api.events.player;

import net.minecraftforge.fml.LogicalSide;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;


/**
 * Additional Player Events that can be used.
 */
public class BattleArtsPlayerEvents
{
    /**
     * Fired when a player dies.
     */
    public static final PlayerEventListener.EventType<PlayerDeathEvent<ServerPlayerPatch>> PLAYER_DEATH_EVENT = new PlayerEventListener.EventType<>(LogicalSide.SERVER);

    /**
     * Used in conjunction with death event.
     */
    public static final PlayerEventListener.EventType<PlayerReviveEvent<ServerPlayerPatch>> PLAYER_REVIVE_EVENT = new PlayerEventListener.EventType<>(LogicalSide.SERVER);
    public static final PlayerEventListener.EventType<EquipmentSwitchEvent<ServerPlayerPatch>> EQUIPMENT_SWITCH_EVENT = new PlayerEventListener.EventType<>(LogicalSide.SERVER);

}
