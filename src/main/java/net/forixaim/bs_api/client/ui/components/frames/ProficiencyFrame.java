package net.forixaim.bs_api.client.ui.components.frames;

import net.forixaim.bs_api.BattleArtsAPI;
import net.forixaim.bs_api.proficiencies.ProficiencyContainer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ProficiencyFrame extends Button
{
	private ProficiencyContainer proficiency;
	private Font currentFont;

	private static final ResourceLocation PROFICIENCY_BACKGROUND = new ResourceLocation(BattleArtsAPI.MOD_ID, "textures/gui/frames/proficiency_frame.png");

	public ProficiencyFrame(int x, int y, int width, int height, ProficiencyContainer proficiency, Component title, OnPress pressAction, Font font)
	{
		super(x, y, width, height, title, pressAction, DEFAULT_NARRATION);
		this.proficiency = proficiency;
		this.currentFont = font;
	}

	@Override
	public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick)
	{
		super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
		pGuiGraphics.blit(PROFICIENCY_BACKGROUND, getX(), getY(), 0, 0, this.width, this.height);
		pGuiGraphics.drawString(currentFont, proficiency.getContainingProficiency().getIdentifier().toString(), getX() + (this.width / 2), getY() + (this.height / 2), 16777215, true);
	}
}
