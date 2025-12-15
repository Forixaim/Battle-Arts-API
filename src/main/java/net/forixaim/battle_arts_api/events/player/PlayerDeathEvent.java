package net.forixaim.battle_arts_api.events.player;

import net.minecraft.world.damagesource.DamageSource;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.entity.eventlistener.AbstractPlayerEvent;

/**
 * Event fired when a player dies.
 * @param <T> ServerPlayerPatch.
 * @author Forixaim
 */
public class PlayerDeathEvent<T extends PlayerPatch<?>> extends AbstractPlayerEvent<T>
{
    private final DamageSource damageSource;
    public PlayerDeathEvent(T playerPatch, DamageSource damageSource)
    {
        super(playerPatch, true);
        this.damageSource = damageSource;
    }

    public DamageSource getDamageSource()
    {
        return this.damageSource;
    }
}
