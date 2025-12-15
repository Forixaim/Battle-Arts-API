package net.forixaim.battle_arts_api.battle_arts_skills;

import net.forixaim.battle_arts_api.BattleArtsAPI;
import net.forixaim.battle_arts_api.battle_arts_skills.active.ActiveSkill;
import net.forixaim.battle_arts_api.battle_arts_skills.battle_style.BattleStyle;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import yesman.epicfight.api.utils.PacketBufferCodec;
import yesman.epicfight.skill.SkillDataKey;
import yesman.epicfight.skill.SkillDataKeys;

/**
 * Core data keys provided by the Battle Arts API.
 * @author Forixaim
 */
public class CoreAPIDataKeys
{
    public static final DeferredRegister<SkillDataKey<?>> DATA_KEYS = DeferredRegister.create(SkillDataKeys.DATA_KEYS.getRegistryKey(), BattleArtsAPI.MOD_ID);

    public static final RegistryObject<SkillDataKey<Float>> METER_FILL = DATA_KEYS.register("meter_fill", () -> SkillDataKey.createSkillDataKey(PacketBufferCodec.FLOAT, 0.0f, true, ActiveSkill.class, BattleStyle.class));
}
