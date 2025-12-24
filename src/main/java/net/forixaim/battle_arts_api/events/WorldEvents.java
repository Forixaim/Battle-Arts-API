package net.forixaim.battle_arts_api.events;

import net.forixaim.battle_arts_api.BattleArtsAPI;
import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.battle_arts_api.battle_arts_skills.battle_style.BattleStyle;
import net.forixaim.battle_arts_api.events.player.BattleArtsPlayerEvents;
import net.forixaim.battle_arts_api.events.player.EquipmentSwitchEvent;
import net.forixaim.battle_arts_api.events.player.PlayerDeathEvent;
import net.forixaim.battle_arts_api.events.player.PlayerReviveEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

@EventBusSubscriber(modid = BattleArtsAPI.MOD_ID)
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

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onEquipmentChangeEvent(LivingEquipmentChangeEvent event)
    {
        if (event.getEntity() instanceof Player player)
        {
            if (player.level().isClientSide)
                return;
            ServerPlayerPatch serverPlayer = EpicFightCapabilities.getEntityPatch(player, ServerPlayerPatch.class);
            serverPlayer.getEventListener().triggerEvents(BattleArtsPlayerEvents.EQUIPMENT_SWITCH_EVENT, new EquipmentSwitchEvent<>(serverPlayer, false, event.getFrom(), event.getTo(), event.getSlot()));

        }
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event)
    {
        if (event.getEntity().level().isClientSide)
            return;
        if (event.getEntity() instanceof ServerPlayer player)
        {
            ServerPlayerPatch serverPlayer = EpicFightCapabilities.getServerPlayerPatch(player);
            if (serverPlayer.getEventListener().triggerEvents(BattleArtsPlayerEvents.PLAYER_DEATH_EVENT, new PlayerDeathEvent<>(serverPlayer, event.getSource())))
            {
                event.setCanceled(true);
                serverPlayer.getEventListener().triggerEvents(BattleArtsPlayerEvents.PLAYER_REVIVE_EVENT, new PlayerReviveEvent<>(serverPlayer));
            }
        }
    }
}
