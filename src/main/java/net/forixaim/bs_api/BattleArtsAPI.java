package net.forixaim.bs_api;

import net.forixaim.bs_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.bs_api.battle_arts_skills.BattleArtsSkillCategories;
import net.forixaim.bs_api.battle_arts_skills.battle_style.BattleStyleCategories;
import net.forixaim.bs_api.battle_arts_skills.battle_style.BattleStyleCategory;
import net.forixaim.bs_api.registry.ManaArtsDataKeys;
import net.forixaim.bs_api.registry.ParticleRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import yesman.epicfight.skill.SkillCategory;
import yesman.epicfight.skill.SkillSlot;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BattleArtsAPI.MOD_ID)
public class BattleArtsAPI
{
    public static final String MOD_ID = "battlearts_api";

    public BattleArtsAPI(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        BattleStyleCategory.ENUM_MANAGER.registerEnumCls(MOD_ID, BattleStyleCategories.class);
        SkillCategory.ENUM_MANAGER.registerEnumCls(MOD_ID, BattleArtsSkillCategories.class);
        SkillSlot.ENUM_MANAGER.registerEnumCls(MOD_ID, BattleArtsSkillSlots.class);
        ManaArtsDataKeys.DATA_KEYS.register(modEventBus);
        ParticleRegistry.PARTICLES.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }


    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
    }

}
