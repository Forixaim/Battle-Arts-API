package net.forixaim.battle_arts_api.battle_arts_skills;

import net.forixaim.battle_arts_api.BattleArtsAPI;
import net.minecraft.resources.ResourceLocation;
import yesman.epicfight.skill.SkillCategory;

public enum BattleArtsSkillCategories implements SkillCategory
{
	BATTLE_STYLE(true, true, true, ResourceLocation.fromNamespaceAndPath(BattleArtsAPI.MOD_ID, "skillbook_battle_style")),
	MANA_ART(true, true, true),
	COMBAT_ART(true, true, false),
	BURST_ART(true, true, false),
	ULTIMATE_ART(true, true, false);

	final boolean Save;
	final boolean Sync;
	final boolean Modifiable;
	final int ID;
    ResourceLocation bookIcon = null;

	BattleArtsSkillCategories(boolean ShouldSave, boolean ShouldSync, boolean Modifiable)
	{
		this.Modifiable = Modifiable;
		this.Save = ShouldSave;
		this.Sync = ShouldSync;
		this.ID = SkillCategory.ENUM_MANAGER.assign(this);
	}

    BattleArtsSkillCategories(boolean ShouldSave, boolean ShouldSync, boolean Modifiable, ResourceLocation bookIcon)
    {
        this.Modifiable = Modifiable;
        this.Save = ShouldSave;
        this.Sync = ShouldSync;
        this.ID = SkillCategory.ENUM_MANAGER.assign(this);
        this.bookIcon = bookIcon;
    }

	@Override
	public boolean shouldSave()
	{
		return this.Save;
	}

	@Override
	public boolean shouldSynchronize()
	{
		return this.Sync;
	}

	@Override
	public boolean learnable()
	{
		return this.Modifiable;
	}
	@Override
	public int universalOrdinal()
	{
		return this.ID;
	}

    @Override
    public ResourceLocation bookIcon() {
        if (bookIcon != null)
        {
            return bookIcon;
        }
        return SkillCategory.super.bookIcon();
    }
}
