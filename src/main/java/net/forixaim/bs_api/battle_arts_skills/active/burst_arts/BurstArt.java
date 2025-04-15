package net.forixaim.bs_api.battle_arts_skills.active.burst_arts;


import net.forixaim.bs_api.battle_arts_skills.BattleArtsSkillCategories;
import net.forixaim.bs_api.battle_arts_skills.active.ActiveSkill;
import yesman.epicfight.skill.SkillBuilder;

/**
 * This class isn't supposed to be used. It's mainly here for organization
 */

public abstract class BurstArt extends ActiveSkill
{

	public static SkillBuilder<BurstArt> createBurstArt()
	{
		return (new SkillBuilder<BurstArt>().setCategory(BattleArtsSkillCategories.BURST_ART).setResource(Resource.COOLDOWN));
	}

	public BurstArt(SkillBuilder<? extends BurstArt> builder) {
		super(builder);
	}

}
