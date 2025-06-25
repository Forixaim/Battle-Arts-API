package net.forixaim.bs_api.battle_arts_skills.mana_arts;

import com.mojang.logging.LogUtils;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.forixaim.bs_api.animations.ManaArtsAnimations;
import net.forixaim.bs_api.client.KeyBinds;
import net.forixaim.bs_api.registry.ManaArtsDataKeys;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.ModList;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.client.events.engine.ControllEngine;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.network.server.SPSkillExecutionFeedback;
import yesman.epicfight.skill.ChargeableSkill;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillBuilder;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.UUID;

public class Charge extends ManaArt implements ChargeableSkill
{
    private static final UUID EVENT_UUID = UUID.fromString("9e069b17-fff4-4ae0-a7ae-79ec84a667dc");
    public Charge(SkillBuilder<? extends Skill> builder)
    {
        super(builder);
    }

    @Override
    public void onInitiate(SkillContainer container)
    {
        super.onInitiate(container);

        container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.MOVEMENT_INPUT_EVENT, EVENT_UUID, event -> {
            if (container.getDataManager().getDataValue(ManaArtsDataKeys.CHARGING.get()))
            {
                event.getMovementInput().forwardImpulse = 0;
                event.getMovementInput().leftImpulse = 0;
                event.getMovementInput().jumping = false;
            }
        });
    }

    @Override
    public void onRemoved(SkillContainer container)
    {
        container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.MOVEMENT_INPUT_EVENT, EVENT_UUID);
        super.onRemoved(container);
    }

    @Override
    public void chargingTick(PlayerPatch<?> caster)
    {
        ChargeableSkill.super.chargingTick(caster);
        caster.getOriginal().level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, caster.getOriginal().getX() + ((caster.getOriginal().getRandom().nextDouble() * 2) - 1) * 0.5, caster.getOriginal().getY(), caster.getOriginal().getZ() + ((caster.getOriginal().getRandom().nextDouble() * 2) - 1) * 0.5, ((caster.getOriginal().getRandom().nextDouble() * 2) - 1) * 0.1, 0.1, ((caster.getOriginal().getRandom().nextDouble() * 2) - 1) * 0.1);
        if (ModList.get().isLoaded(IronsSpellbooks.MODID))
        {
            try
            {
                if (MagicData.getPlayerMagicData(caster.getOriginal()).getMana() < caster.getOriginal().getAttributeValue(AttributeRegistry.MAX_MANA.get()))
                {
                    int manaCharge = (int) (caster.getOriginal().getAttributeValue(AttributeRegistry.MAX_MANA.get()) / 200);
                    MagicData.getPlayerMagicData(caster.getOriginal()).addMana(manaCharge);
                }
            }
            catch (Exception e)
            {

            }
            
        }
    }

    @Override
    public void startCharging(PlayerPatch<?> playerPatch)
    {
        LogUtils.getLogger().debug("TestCharge");
        if (!playerPatch.isLogicalClient())
        {
            playerPatch.getSkill(this).getDataManager().setDataSync(ManaArtsDataKeys.CHARGING.get(), true, (ServerPlayer) playerPatch.getOriginal());
        }
        playerPatch.getEntityState().setState(EntityState.MOVEMENT_LOCKED, true);
        playerPatch.playAnimationSynchronized(ManaArtsAnimations.CHARGE_1, 0);
    }

    @Override
    public void resetCharging(PlayerPatch<?> playerPatch)
    {
        if (playerPatch.isLogicalClient()) {
            playerPatch.getSkill(this).getDataManager().setDataSync(ManaArtsDataKeys.CHARGING.get(), false, (LocalPlayer) playerPatch.getOriginal());
            playerPatch.getAnimator().stopPlaying(ManaArtsAnimations.CHARGE_1);
        } else {
            playerPatch.getSkill(this).getDataManager().setDataSync(ManaArtsDataKeys.CHARGING.get(), false, (ServerPlayer) playerPatch.getOriginal());
            playerPatch.stopPlaying(ManaArtsAnimations.CHARGE_1);
        }
        playerPatch.getEntityState().setState(EntityState.MOVEMENT_LOCKED, false);
    }

    @Override
    public int getAllowedMaxChargingTicks()
    {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getMaxChargingTicks()
    {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getMinChargingTicks()
    {
        return 0;
    }

    @Override
    public void castSkill(ServerPlayerPatch serverPlayerPatch, SkillContainer skillContainer, int i, SPSkillExecutionFeedback spSkillExecutionFeedback, boolean b)
    {
        serverPlayerPatch.getAnimator().stopPlaying(ManaArtsAnimations.CHARGE_1);
        serverPlayerPatch.playAnimationSynchronized(ManaArtsAnimations.CHARGE_RELEASE_1, 0);
        skillContainer.getDataManager().setDataSync(ManaArtsDataKeys.CHARGING.get(), false, serverPlayerPatch.getOriginal());
        serverPlayerPatch.getEntityState().setState(EntityState.MOVEMENT_LOCKED, false);
        this.cancelOnServer(skillContainer, null);

    }

    @Override
    public void gatherChargingArguemtns(LocalPlayerPatch localPlayerPatch, ControllEngine controllEngine, FriendlyByteBuf friendlyByteBuf)
    {

    }

    @Override
    public KeyMapping getKeyMapping()
    {
        return KeyBinds.USE_MANA_ART;
    }
}
