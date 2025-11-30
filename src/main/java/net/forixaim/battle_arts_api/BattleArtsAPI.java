package net.forixaim.battle_arts_api;

import com.yesman.epicskills.EpicSkills;
import com.yesman.epicskills.client.gui.screen.CategorySlotTexture;
import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillCategories;
import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsTextures;
import net.forixaim.battle_arts_api.battle_arts_skills.CoreAPIDataKeys;
import net.forixaim.battle_arts_api.battle_arts_skills.battle_style.BattleStyleCategories;
import net.forixaim.battle_arts_api.battle_arts_skills.battle_style.BattleStyleCategory;
import net.forixaim.battle_arts_api.client.input.action.BattleArtsInputAction;
import net.forixaim.battle_arts_api.registry.ManaArtsDataKeys;
import net.forixaim.battle_arts_api.registry.ParticleRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import yesman.epicfight.api.client.input.action.InputAction;
import yesman.epicfight.skill.SkillCategory;
import yesman.epicfight.skill.SkillSlot;

/**
 * This is the Battle Arts API used for memes and funnies.
 * @author Forixaim
 */
@Mod(BattleArtsAPI.MOD_ID)
public class BattleArtsAPI
{
    //Local Debug Controls
    public static final boolean debugMode = false;
    public static final String MOD_ID = "battlearts_api";

    public BattleArtsAPI(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        BattleStyleCategory.ENUM_MANAGER.registerEnumCls(MOD_ID, BattleStyleCategories.class);
        if (ModList.get().isLoaded(EpicSkills.MODID) && FMLEnvironment.dist == Dist.CLIENT)
            CategorySlotTexture.ENUM_MANAGER.registerEnumCls(MOD_ID, BattleArtsTextures.class);
        SkillCategory.ENUM_MANAGER.registerEnumCls(MOD_ID, BattleArtsSkillCategories.class);
        SkillSlot.ENUM_MANAGER.registerEnumCls(MOD_ID, BattleArtsSkillSlots.class);
        CoreAPIDataKeys.DATA_KEYS.register(modEventBus);
        ManaArtsDataKeys.DATA_KEYS.register(modEventBus);
        ParticleRegistry.PARTICLES.register(modEventBus);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            InputAction.ENUM_MANAGER.registerEnumCls(MOD_ID, BattleArtsInputAction.class);
        }

        MinecraftForge.EVENT_BUS.register(this);
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

}
