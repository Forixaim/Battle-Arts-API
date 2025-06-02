package net.forixaim.bs_api.battle_arts_skills.battle_style.starting;

import net.forixaim.bs_api.battle_arts_skills.battle_style.BattleStyle;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.skill.Skill;

public class Traveler extends BattleStyle
{
	public Traveler(Builder<? extends Skill> builder)
	{
		super(builder);
		proficiencyXpPerKill = 1;
		this.unarmedBattleMotions.put(
				LivingMotions.IDLE,
				Animations.BIPED_HOLD_LONGSWORD
		);
	}
}
