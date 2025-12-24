package net.forixaim.battle_arts_api;

import com.yesman.epicskills.EpicSkills;
import com.yesman.epicskills.client.gui.screen.CategorySlotTexture;
import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsTextures;
import net.forixaim.battle_arts_api.client.input.action.BattleArtsInputAction;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import yesman.epicfight.api.client.input.action.InputAction;

@Mod(value = BattleArtsAPI.MOD_ID, dist = Dist.CLIENT)
public class BattleArtsAPIClient {
    public BattleArtsAPIClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        registerEnums();
    }

    public void registerEnums() {
        if (ModList.get().isLoaded(EpicSkills.MODID)) {
            CategorySlotTexture.ENUM_MANAGER.registerEnumCls(BattleArtsAPI.MOD_ID, BattleArtsTextures.class);
        }
        InputAction.ENUM_MANAGER.registerEnumCls(BattleArtsAPI.MOD_ID, BattleArtsInputAction.class);
    }
}
