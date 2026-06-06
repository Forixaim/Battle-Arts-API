package net.forixaim.battle_arts_api.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.forixaim.battle_arts_api.generated.LangKeys;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinds
{
	public static final KeyMapping USE_ART_1 = mapping(LangKeys.KEY_COMBAT_ART_USE, GLFW.GLFW_KEY_Z);
	public static final KeyMapping USE_BURST_ART = mapping(LangKeys.KEY_BURST_ART_USE, GLFW.GLFW_KEY_C);
	public static final KeyMapping USE_ULTIMATE_ART = mapping(LangKeys.KEY_ULTIMATE_ART_USE, GLFW.GLFW_KEY_V);
	public static final KeyMapping USE_SPECIAL_ART = mapping(LangKeys.KEY_SPECIAL_ART_USE, GLFW.GLFW_KEY_X);

	private static KeyMapping mapping(String name, int key) {
		return new KeyMapping(name, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, key, LangKeys.KEY_CATEGORY_SKILLS);
	}

}
