package net.forixaim.bs_api.client.ui;

import com.google.common.collect.Lists;
import net.forixaim.bs_api.BattleArtsAPI;
import net.forixaim.bs_api.capabilities.ProficiencyCapabilityProvider;
import net.forixaim.bs_api.client.ui.components.frames.ProficiencyFrame;
import net.forixaim.bs_api.proficiencies.ProficiencyContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ProficiencyScreen extends Screen
{
	private static final ResourceLocation PROFICIENCY_GUI_BACKGROUND = new ResourceLocation(BattleArtsAPI.MOD_ID, "textures/gui/proficiencies.png");
	private final Player player;
	private final List<ProficiencyFrame> proficiencies;

	public ProficiencyScreen(Player player)
	{
		super(Component.translatable("gui.bs_api.proficiency.view"));
		this.player = player;
		proficiencies = Lists.newArrayList();
	}

	@Override
	protected void init()
	{
		int cornerX = this.width / 2 - 124;
		int cornerY = this.height / 2 - 60;
		List<ProficiencyContainer> proficiencyContainers = Lists.newArrayList();
		player.getCapability(ProficiencyCapabilityProvider.PROFICIENCY_CAPABILITY).ifPresent(
				proficiencyCapability -> proficiencyContainers.addAll(proficiencyCapability.getProficiencies())
		);
		int c = 0;
		for (ProficiencyContainer container : proficiencyContainers)
		{
			proficiencies.add(new ProficiencyFrame(cornerX, cornerY, (256 - 16) / 5, (128 - 8) / 2, container, Component.literal("test"), button -> {}, this.font));
			cornerX += (256 - 16) / 5;
			c++;
			if (c == 5)
			{
				cornerX = this.width / 2 - 124;
				cornerY += (128 - 8) / 2;
			}
		}
		for (ProficiencyFrame proficiencyFrame : proficiencies)
		{
			this.addRenderableOnly(proficiencyFrame);
		}
	}

	@Override
	public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick)
	{
		renderBackground(pGuiGraphics);
		if (!proficiencies.isEmpty())
		{
			for (ProficiencyFrame frame : proficiencies)
			{
				frame.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
			}
		}
	}

	@Override
	public void renderBackground(@NotNull GuiGraphics pGuiGraphics)
	{
		super.renderBackground(pGuiGraphics);
		pGuiGraphics.blit(PROFICIENCY_GUI_BACKGROUND, this.width / 2 - 128, this.height / 2 - 64, 0, 0, 219, 128);
	}
}
