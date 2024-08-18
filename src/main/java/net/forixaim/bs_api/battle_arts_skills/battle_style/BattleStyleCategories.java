package net.forixaim.bs_api.battle_arts_skills.battle_style;

public enum BattleStyleCategories implements BattleStyleCategory
{
	STARTING,
	NOVICE,
	ADVANCED,
	ELITE,
	MASTER,
	UNIQUE,
	LEGENDARY;

	final int id;

	BattleStyleCategories()
	{
		id = BattleStyleCategory.ENUM_MANAGER.assign(this);
	}

	@Override
	public int universalOrdinal()
	{
		return id;
	}
}
