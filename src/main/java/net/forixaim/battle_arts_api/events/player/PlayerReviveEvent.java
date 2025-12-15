package net.forixaim.battle_arts_api.events.player;

import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.entity.eventlistener.AbstractPlayerEvent;

/**
 * Event fired when a player death event is canceled.
 * @param <T> ServerPlayerPatch
 * @author Forixaim
 */
public class PlayerReviveEvent<T extends PlayerPatch<?>> extends AbstractPlayerEvent<T>
{

    public PlayerReviveEvent(T playerPatch)
    {
        super(playerPatch, false);
    }
}
