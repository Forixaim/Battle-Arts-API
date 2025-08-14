package net.forixaim.battle_arts_api.events;

import net.forixaim.battle_arts_api.BattleArtsAPI;
import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.battle_arts_api.battle_arts_skills.battle_style.BattleStyle;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@Mod.EventBusSubscriber(modid = BattleArtsAPI.MOD_ID)
public class WorldEvents
{
    @SubscribeEvent
    public static void onPlayerJump(LivingEvent.LivingJumpEvent event)
    {
        if (EpicFightCapabilities.getEntityPatch(event.getEntity(), LivingEntityPatch.class) instanceof PlayerPatch<?> player)
        {
            if (player.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getSkill() instanceof BattleStyle bs)
            {
                Vec3 dM = player.getOriginal().getDeltaMovement();
                event.getEntity().setDeltaMovement(dM.x, dM.y * (1+ bs.getJumpBoostPower()), dM.z);
            }
        }
    }
}
