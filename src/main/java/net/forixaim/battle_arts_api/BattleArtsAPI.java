package net.forixaim.battle_arts_api;

import com.mojang.logging.LogUtils;
import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillCategories;
import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.battle_arts_api.battle_arts_skills.CoreAPIDataKeys;
import net.forixaim.battle_arts_api.battle_arts_skills.battle_style.BattleStyleCategories;
import net.forixaim.battle_arts_api.battle_arts_skills.battle_style.BattleStyleCategory;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;
import yesman.epicfight.skill.SkillCategory;
import yesman.epicfight.skill.SkillSlot;

/**
 * <p>This is the Battle Arts API used for almost every Battle Arts Suite of mods</p>
 * @author Forixaim
 */
@Mod(BattleArtsAPI.MOD_ID)
public class BattleArtsAPI
{
    //Local Debug Controls
    public static final boolean debugMode = false;
    public static final String MOD_ID = "battlearts_api";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceLocation identifier(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public BattleArtsAPI(IEventBus modEventBus, ModContainer modContainer) {
        registerEnums();
        registerRegistries(modEventBus);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    public void registerEnums()
    {
        BattleStyleCategory.ENUM_MANAGER.registerEnumCls(MOD_ID, BattleStyleCategories.class);
        SkillCategory.ENUM_MANAGER.registerEnumCls(MOD_ID, BattleArtsSkillCategories.class);
        SkillSlot.ENUM_MANAGER.registerEnumCls(MOD_ID, BattleArtsSkillSlots.class);
    }

    public void registerRegistries(IEventBus modEventBus)
    {
        CoreAPIDataKeys.DATA_KEYS.register(modEventBus);
    }
}
