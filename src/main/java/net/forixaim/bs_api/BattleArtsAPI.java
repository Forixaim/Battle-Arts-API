package net.forixaim.bs_api;

import com.mojang.logging.LogUtils;
import net.forixaim.bs_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.bs_api.battle_arts_skills.BattleArtsSkillCategories;
import net.forixaim.bs_api.battle_arts_skills.battle_style.BattleStyleCategories;
import net.forixaim.bs_api.battle_arts_skills.battle_style.BattleStyleCategory;
import net.forixaim.bs_api.cmd.ProficiencyCommand;
import net.forixaim.bs_api.proficiencies.ProficiencyManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import yesman.epicfight.skill.SkillCategory;
import yesman.epicfight.skill.SkillSlot;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BattleArtsAPI.MOD_ID)
public class BattleArtsAPI
{
    public static final String MOD_ID = "bs_api";
    private static final Logger LOGGER = LogUtils.getLogger();
    public BattleArtsAPI() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BattleStyleCategory.ENUM_MANAGER.registerEnumCls(MOD_ID, BattleStyleCategories.class);
        SkillCategory.ENUM_MANAGER.registerEnumCls(MOD_ID, BattleArtsSkillCategories.class);
        SkillSlot.ENUM_MANAGER.registerEnumCls(MOD_ID, BattleArtsSkillSlots.class);
        //Register Registries
        modEventBus.addListener(ProficiencyManager::createProficiencyRegistry);
        modEventBus.addListener(ProficiencyManager::registerProficiencies);


        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.addListener(this::regTestCommands);
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void regTestCommands(final RegisterCommandsEvent event)
    {
        ProficiencyCommand.register(event.getDispatcher());
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
    }

}
