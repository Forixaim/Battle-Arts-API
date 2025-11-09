package net.forixaim.battle_arts_api.battle_arts_skills.battle_style.starting;

import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.battle_arts_api.battle_arts_skills.NetworkUtils;
import net.forixaim.battle_arts_api.battle_arts_skills.battle_style.BattleStyle;
import net.forixaim.battle_arts_api.registry.BattleStyleRegistry;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;

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

    @Override
    public void onInitiate(SkillContainer container) {
        super.onInitiate(container);
        NetworkUtils.changeSkill(container.getExecutor(), BattleArtsSkillSlots.COMBAT_ART, BattleStyleRegistry.EXAMPLE_COMBAT_ART);
    }

    @Override
    public void onRemoved(SkillContainer container) {
        super.onRemoved(container);
        NetworkUtils.changeSkill(container.getExecutor(), BattleArtsSkillSlots.COMBAT_ART, null);

    }
}
