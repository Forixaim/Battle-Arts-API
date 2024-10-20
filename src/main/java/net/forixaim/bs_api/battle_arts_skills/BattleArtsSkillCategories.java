package net.forixaim.bs_api.battle_arts_skills;

import yesman.epicfight.skill.SkillCategory;

public enum BattleArtsSkillCategories implements SkillCategory
{
	BATTLE_STYLE(true, true, true),
	TAUNT(true, true, false),
	COMBAT_ART(true, true, true),
	BURST_ART(true, true, true),
	ULTIMATE_ART(true, true, true);

	final boolean Save;
	final boolean Sync;
	final boolean Modifiable;
	final int ID;

	BattleArtsSkillCategories(boolean ShouldSave, boolean ShouldSync, boolean Modifiable)
	{
		this.Modifiable = Modifiable;
		this.Save = ShouldSave;
		this.Sync = ShouldSync;
		this.ID = SkillCategory.ENUM_MANAGER.assign(this);
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
}
