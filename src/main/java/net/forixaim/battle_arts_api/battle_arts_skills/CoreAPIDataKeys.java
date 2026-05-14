package net.forixaim.battle_arts_api.battle_arts_skills;

import net.forixaim.battle_arts_api.BattleArtsAPI;
import net.forixaim.battle_arts_api.battle_arts_skills.active.ActiveSkill;
import net.forixaim.battle_arts_api.battle_arts_skills.battle_style.BattleStyle;

import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import yesman.epicfight.registry.EpicFightRegistries;
import yesman.epicfight.skill.SkillDataKey;

/**
 * Core data keys provided by the Battle Arts API.
 * @author Forixaim
 */
public class CoreAPIDataKeys
{
    public static final DeferredRegister<SkillDataKey<?>> DATA_KEYS = DeferredRegister.create(EpicFightRegistries.SKILL_DATA_KEY, BattleArtsAPI.MOD_ID);
    public static final DeferredHolder<SkillDataKey<?>, SkillDataKey<Float>> METER_FILL = DATA_KEYS.register("meter_fill", () -> SkillDataKey.createSkillDataKey(ByteBufCodecs.FLOAT, 0.0f, true, ActiveSkill.class, BattleStyle.class));
}
