package net.forixaim.bs_api.battle_arts_skills.battle_style.starting;

import net.forixaim.bs_api.battle_arts_skills.battle_style.BattleStyle;
import net.forixaim.bs_api.tests.DummyAnimations;
import reascer.wom.gameasset.WOMAnimations;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.skill.Skill;

import java.util.Arrays;

public class Traveler extends BattleStyle
{
	public Traveler(Builder<? extends Skill> builder)
	{
		super(builder);
		proficiencyXpPerKill = 1;

		this.unarmedLivingMotions.put(
				LivingMotions.IDLE,
				() -> DummyAnimations.T_POSE
		);
		this.unarmedBattleMotions.put(
				LivingMotions.IDLE,
				() -> Animations.BIPED_HOLD_LONGSWORD
		);
	}
}
