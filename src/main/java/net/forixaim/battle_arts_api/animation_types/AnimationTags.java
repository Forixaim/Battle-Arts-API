package net.forixaim.battle_arts_api.animation_types;

import net.forixaim.battle_arts_api.BattleArtsAPI;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public interface AnimationTags
{
    TagKey<DamageType> SLASH = create("slash");
    TagKey<DamageType> PUNCTURE = create("puncture");
    TagKey<DamageType> IMPACT = create("impact");

    private static TagKey<DamageType> create(String tagName) {
        return TagKey.create(Registries.DAMAGE_TYPE, BattleArtsAPI.identifier(tagName));
    }
}
