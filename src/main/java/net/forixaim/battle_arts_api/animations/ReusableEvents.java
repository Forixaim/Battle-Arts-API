package net.forixaim.battle_arts_api.animations;

import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

public class ReusableEvents
{
    public static final AnimationEvent.E0 PLAY_STEP_SOUND = ((livingEntityPatch, assetAccessor, animationParameters) ->
    {
       if (livingEntityPatch instanceof PlayerPatch<?> playerPatch)
       {
           playerPatch.getOriginal().playStepSound(
                   playerPatch.getOriginal().getOnPos(), playerPatch.getOriginal().level().getBlockState(playerPatch.getOriginal().getOnPos())
           );
           if (playerPatch.getOriginal().shouldPlayAmethystStepSound(playerPatch.getOriginal().level().getBlockState(playerPatch.getOriginal().getOnPos())))
               playerPatch.getOriginal().playAmethystStepSound();

       }
    });
}
