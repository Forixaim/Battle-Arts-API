package net.forixaim.battle_arts_api.battle_arts_skills.active.combat_arts;


import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillCategories;
import net.forixaim.battle_arts_api.battle_arts_skills.active.ActiveSkill;
import yesman.epicfight.skill.SkillBuilder;
import yesman.epicfight.skill.SkillContainer;

/**
 * This class isn't supposed to be used. It's mainly here for organization
 */

public abstract class CombatArt extends ActiveSkill
{

	public static SkillBuilder<CombatArt> createCombatArt()
	{
		return (new SkillBuilder<CombatArt>().setCategory(BattleArtsSkillCategories.COMBAT_ART).setResource(Resource.COOLDOWN));
	}

	public CombatArt(SkillBuilder<? extends CombatArt> builder) {
		super(builder);
	}

	@Override
	public void onInitiate(SkillContainer container)
	{
		super.onInitiate(container);

	}

}
