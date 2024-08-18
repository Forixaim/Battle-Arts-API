package net.forixaim.bs_api.battle_arts_skills.battle_style.starting;

import net.forixaim.bs_api.battle_arts_skills.battle_style.BattleStyle;
import yesman.epicfight.skill.Skill;

public class Noble extends BattleStyle
{
	public Noble(Builder<? extends Skill> builder)
	{
		super(builder);
		proficiencyXpPerKill = 1;
	}
}
