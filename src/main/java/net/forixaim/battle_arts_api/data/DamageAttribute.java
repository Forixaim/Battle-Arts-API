package net.forixaim.battle_arts_api.data;

import net.forixaim.battle_arts_api.animation_types.AnimationTags;
import net.forixaim.battle_arts_api.registry.BattleArtsCustomData;
import net.minecraft.nbt.CompoundTag;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;

import java.util.Optional;

public record DamageAttribute(float slash, float puncture, float impact)
{
    public static final float SLASH_DAMAGE_MODIFIER = 1.05f;
    public static final float PUNCTURE_DAMAGE_MODIFIER = 0.9f;
    public static final float IMPACT_DAMAGE_MODIFIER = 1.0f;

    public static final float REND_DAMAGE_MODIFIER = 1.1f;
    public static final float PIERCE_DAMAGE_MODIFIER = 1.0f;
    public static final float CLEAVE_DAMAGE_MODIFIER = 1.15f;



    public static final DamageAttribute DEFAULT = new DamageAttribute(1, 1, 1);
    public static DamageAttribute deserialize(CompoundTag tag)
    {
        float slash = 1;
        float puncture = 1;
        float impact = 1;
        if (tag.contains("slash", CompoundTag.TAG_DOUBLE))
        {
            slash = tag.getFloat("slash");
        }
        if (tag.contains("puncture", CompoundTag.TAG_DOUBLE))
        {
            puncture = tag.getFloat("puncture");
        }
        if (tag.contains("impact", CompoundTag.TAG_DOUBLE))
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
        Optional<DamageAttribute> attribute = item.getCustomData(BattleArtsCustomData.DAMAGE_ATTRIBUTE_MODIFIER);
        if (attribute.isEmpty())
        {
            return 1.0f;
        }
        if (source.is(AnimationTags.REND))
        {
            return (attribute.get().slash + attribute.get().puncture()) / 2;
        }
        if (source.is(AnimationTags.PIERCE))
        {
            return (attribute.get().puncture() + attribute.get().impact()) / 2;
        }
        if (source.is(AnimationTags.CLEAVE))
        {
            return (attribute.get().slash() + attribute.get().impact()) / 2;
        }
        if (source.is(AnimationTags.SLASH))
        {
            return attribute.get().slash;
        }
        if (source.is(AnimationTags.PUNCTURE))
        {
            source.attachImpactModifier(ValueModifier.adder(5f));
            return attribute.get().puncture;
        }
        if (source.is(AnimationTags.IMPACT))
        {
            return attribute.get().impact;
        }
        return 1.0f;
    }
}
