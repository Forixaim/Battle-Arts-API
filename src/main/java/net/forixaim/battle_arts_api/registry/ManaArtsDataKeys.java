package net.forixaim.battle_arts_api.registry;

import net.forixaim.battle_arts_api.BattleArtsAPI;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.skill.SkillDataKey;

public class ManaArtsDataKeys
{
    public static DeferredRegister<SkillDataKey<?>> DATA_KEYS = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(EpicFightMod.MODID, "skill_data_keys"), BattleArtsAPI.MOD_ID);
}
