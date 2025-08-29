package net.forixaim.battle_arts_api.animations;

import eu.ha3.presencefootsteps.PresenceFootsteps;
import eu.ha3.presencefootsteps.sound.State;
import eu.ha3.presencefootsteps.world.Association;
import net.forixaim.battle_arts_api.Config;
import net.minecraftforge.fml.ModList;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

public class ReusableEvents
{
    public static final AnimationEvent.E0 PLAY_STEP_SOUND = ((livingEntityPatch, assetAccessor, animationParameters) ->
    {
       if (livingEntityPatch instanceof PlayerPatch<?> playerPatch && playerPatch.isEpicFightMode() && Config.allowSoundOverrides)
       {
           playerPatch.getOriginal().playStepSound(playerPatch.getOriginal().getOnPos(), playerPatch.getOriginal().level().getBlockState(playerPatch.getOriginal().getOnPos()));
           if (playerPatch.getOriginal().shouldPlayAmethystStepSound(playerPatch.getOriginal().level().getBlockState(playerPatch.getOriginal().getOnPos())))
               playerPatch.getOriginal().playAmethystStepSound();
       }
    });
}
