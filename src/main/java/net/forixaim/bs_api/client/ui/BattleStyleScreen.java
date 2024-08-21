package net.forixaim.bs_api.client.ui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.forixaim.bs_api.BattleArtsAPI;
import net.forixaim.bs_api.battle_arts_skills.BattleArtsSkillCategories;
import net.forixaim.bs_api.battle_arts_skills.battle_style.BattleStyle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import yesman.epicfight.api.data.reloader.SkillManager;
import yesman.epicfight.client.gui.screen.SkillBookScreen;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillSlot;
import yesman.epicfight.world.capabilities.skill.CapabilitySkill;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class BattleStyleScreen extends Screen
{
	private static final ResourceLocation BATTLE_STYLE_UI = new ResourceLocation(BattleArtsAPI.MOD_ID, "textures/gui/battle_style.png");
	private final CapabilitySkill skills;
	private final List<BattleStyleButton> battleStyleButtons = Lists.newArrayList();
	private final Player player;
	private int start;

	private static final int MAX_SHOWING_BUTTONS = 5;

	public BattleStyleScreen(Player player, CapabilitySkill skills)
	{
		super(Component.translatable("ui.battle_style.title"));
		this.skills = skills;
		this.player = player;
	}

	@Override
	public void renderBackground(@NotNull GuiGraphics guiGraphics)
	{
		super.renderBackground(guiGraphics);
		guiGraphics.blit(BATTLE_STYLE_UI, this.width / 2 - 104, this.height / 2 - 100, 0, 0, 208, 200);
	}

	@Override
	public void init()
	{
		int i = this.width / 2 - 96;
		int j = this.height / 2 - 82;

		this.battleStyleButtons.clear();

		Collection<Skill> battleStyles = SkillManager.getSkills((skill) -> skill.getCategory() == BattleArtsSkillCategories.BATTLE_STYLE);

		for (SkillSlot skillSlot : SkillSlot.ENUM_MANAGER.universalValues())
		{
			if ((this.player.isCreative() || this.skills.hasCategory(skillSlot.category())) && skillSlot.category() == BattleArtsSkillCategories.BATTLE_STYLE)
			{
				for (Skill skill : battleStyles)
				{
					if (skill instanceof BattleStyle battleStyle)
						this.battleStyleButtons.add(new BattleStyleButton(0, 0, 180, 20, battleStyle, Component.translatable(battleStyle.getTranslationKey()), (action) -> {

						}));
				}
			}
		}
	}

	@Override
	public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(guiGraphics);
	}

	private boolean canScroll()
	{
		return this.battleStyleButtons.size() > MAX_SHOWING_BUTTONS;
	}

	private boolean canPress(Button button)
	{
		int buttonOrder = this.battleStyleButtons.indexOf(button);

		return buttonOrder >= this.start && buttonOrder <= this.start + MAX_SHOWING_BUTTONS;
	}

	class BattleStyleButton extends Button
	{
		private final BattleStyle skill;

		public BattleStyleButton(int x, int y, int width, int height, BattleStyle skill, Component title, OnPress pressAction)
		{
			super(x, y, width, height, title, pressAction, Button.DEFAULT_NARRATION);
			this.skill = skill;
		}

		@Override
		public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
		{
			this.isHovered = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
			int texY = (this.isHovered || !this.active) ? 224 : 200;
			guiGraphics.blit(BATTLE_STYLE_UI, this.getX(), this.getY(), 0, texY, this.width, this.height);

			RenderSystem.enableBlend();
			guiGraphics.blit(this.skill.getSkillTexture(), this.getX() + 5, this.getY() + 4, 16, 16, 0, 0, 128, 128, 128, 128);
			guiGraphics.drawString(BattleStyleScreen.this.font, this.getMessage(), this.getX() + 26, this.getY() + 2, -1, false);

			if (this.active)
			{
				assert BattleStyleScreen.this.minecraft != null;
				assert BattleStyleScreen.this.minecraft.player != null;
				int color = (BattleStyleScreen.this.minecraft.player.experienceLevel >= this.skill.getRequiredXp() || BattleStyleScreen.this.minecraft.player.isCreative()) ? 8453920 : 16736352;
				guiGraphics.drawString(BattleStyleScreen.this.font, Component.translatable("gui.epicfight.changing_cost", this.skill.getRequiredXp()), this.getX() + 70, this.getY() + 12, color, false);
			} else
			{
				guiGraphics.drawString(BattleStyleScreen.this.font, Component.literal(
						BattleStyleScreen.this.skills.getSkillContainer(this.skill).getSlot().toString().toLowerCase(Locale.ROOT)), this.getX() + 26, this.getY() + 12, 16736352, false);
			}
		}

		@Override
		public boolean mouseClicked(double x, double y, int pressType)
		{
			if (this.visible && pressType == 1)
			{
				boolean flag = this.clickedNoCountActive(x, y);

				if (flag)
				{
					this.playDownSound(Minecraft.getInstance().getSoundManager());
					assert BattleStyleScreen.this.minecraft != null;
					BattleStyleScreen.this.minecraft.setScreen(new SkillBookScreen(BattleStyleScreen.this.player, this.skill, null, BattleStyleScreen.this));
					return true;
				}
			}

			return super.mouseClicked(x, y, pressType);
		}

		protected boolean clickedNoCountActive(double x, double y)
		{
			return this.visible && x >= (double) this.getX() && y >= (double) this.getY() && x < (double) (this.getX() + this.width) && y < (double) (this.getY() + this.height);
		}

		public BattleStyleScreen.BattleStyleButton setActive(boolean active)
		{
			this.active = active;
			return this;
		}
	}
}
