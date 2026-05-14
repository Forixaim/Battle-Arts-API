package net.forixaim.battle_arts_api.data;

import net.forixaim.battle_arts_api.animation_types.AnimationTags;
import net.forixaim.battle_arts_api.registry.BattleArtsCustomData;
import net.minecraft.nbt.CompoundTag;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;

import java.util.Optional;

public record DamageAttribute(float slash, float puncture, float impact)
{
    public static final DamageAttribute DEFAULT = new DamageAttribute(1, 1, 1);
    public static DamageAttribute deserialize(CompoundTag tag)
    {
        float slash = 1;
        float puncture = 1;
        float impact = 1;
        if (tag.contains("slash", CompoundTag.TAG_FLOAT))
        {
            slash = tag.getFloat("slash");
        }
        if (tag.contains("puncture", CompoundTag.TAG_FLOAT))
        {
            puncture = tag.getFloat("puncture");
        }
        if (tag.contains("impact", CompoundTag.TAG_FLOAT))
        {
            impact = tag.getFloat("impact");
        }
        return new DamageAttribute(slash, puncture, impact);
    }

    public static float calculateDamageMultiplier(EpicFightDamageSource source)
    {
        if (EpicFightCapabilities.getItemStackCapability(source.getUsedItem()).isEmpty())
        {
            return 1.0f;
        }
        CapabilityItem item = EpicFightCapabilities.getItemStackCapability(source.getUsedItem());
        Optional<DamageAttribute> attribute = item.getCustomData(BattleArtsCustomData.SLASH_MODIFIER);
        if (attribute.isEmpty())
        {
            return 1.0f;
        }
        if (source.is(AnimationTags.SLASH))
        {
            return attribute.get().slash;
        }
        if (source.is(AnimationTags.PUNCTURE))
        {
            return attribute.get().puncture;
        }
        if (source.is(AnimationTags.IMPACT))
        {
            return attribute.get().impact;
        }
        return 1.0f;
    }
}
