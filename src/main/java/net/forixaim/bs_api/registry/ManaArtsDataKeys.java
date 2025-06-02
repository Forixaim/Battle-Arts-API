package net.forixaim.bs_api.registry;

import net.forixaim.bs_api.BattleArtsAPI;
import net.forixaim.bs_api.battle_arts_skills.mana_arts.Charge;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import yesman.epicfight.api.utils.PacketBufferCodec;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.skill.SkillDataKey;

public class ManaArtsDataKeys
{
    public static DeferredRegister<SkillDataKey<?>> DATA_KEYS = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(EpicFightMod.MODID, "skill_data_keys"), BattleArtsAPI.MOD_ID);

    public static RegistryObject<SkillDataKey<Boolean>> CHARGING = DATA_KEYS.register("charging", () ->
            SkillDataKey.createSkillDataKey(PacketBufferCodec.BOOLEAN, false, true, Charge.class));
}
