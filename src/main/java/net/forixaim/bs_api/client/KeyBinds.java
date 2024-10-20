package net.forixaim.bs_api.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.forixaim.bs_api.BattleArtsAPI;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinds
{
	public static final String CATEGORY_EFBS_SKILLS = "key.category.bs_api.skills";
	public static final String VIEW_PROFICIENCIES = "key.bs_api.view_proficiencies";
	public static final String BATTLE_STYLE_MENU = "key.bs_api.battle_style_menu";
	public static final String COMBAT_ART_1 = "key." + BattleArtsAPI.MOD_ID + ".combat_art_1_use";
	public static final String TAUNT = "key." + BattleArtsAPI.MOD_ID + ".taunt_use";
	public static final String BURST_ART = "key." + BattleArtsAPI.MOD_ID + ".burst_art_use";
	public static final String ULTIMATE_ART = "key." + BattleArtsAPI.MOD_ID + ".ultimate_art_use";

	public static final KeyMapping SHOW_PROFICIENCY_MENU = new KeyMapping(VIEW_PROFICIENCIES, KeyConflictContext.IN_GAME,
			InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_SEMICOLON, CATEGORY_EFBS_SKILLS);
	public static final KeyMapping SHOW_BATTLE_STYLE_MENU = new KeyMapping(BATTLE_STYLE_MENU, KeyConflictContext.IN_GAME,
			InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_APOSTROPHE, CATEGORY_EFBS_SKILLS);
	public static final KeyMapping USE_ART_1 = new KeyMapping(COMBAT_ART_1, KeyConflictContext.IN_GAME,
			InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Z, CATEGORY_EFBS_SKILLS);
	public static final KeyMapping USE_TAUNT = new KeyMapping(TAUNT, KeyConflictContext.IN_GAME,
			InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_X, CATEGORY_EFBS_SKILLS);
	public static final KeyMapping USE_BURST_ART = new KeyMapping(BURST_ART, KeyConflictContext.IN_GAME,
			InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_C, CATEGORY_EFBS_SKILLS);
	public static final KeyMapping USE_ULTIMATE_ART = new KeyMapping(ULTIMATE_ART, KeyConflictContext.IN_GAME,
			InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, CATEGORY_EFBS_SKILLS);

}
