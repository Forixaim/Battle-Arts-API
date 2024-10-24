package net.forixaim.bs_api.battle_arts_skills.battle_style.starting;

import net.forixaim.bs_api.battle_arts_skills.battle_style.BattleStyle;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.skill.Skill;

import java.util.Arrays;

public class Traveler extends BattleStyle
{
	public Traveler(Builder<? extends Skill> builder)
	{
		super(builder);
		proficiencyXpPerKill = 1;
	}
}
