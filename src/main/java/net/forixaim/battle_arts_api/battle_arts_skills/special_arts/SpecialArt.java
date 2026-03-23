package net.forixaim.battle_arts_api.battle_arts_skills.special_arts;

import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillCategories;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillBuilder;

import java.util.function.Function;


public abstract class SpecialArt extends Skill
{
    public SpecialArt(SkillBuilder<?> builder)
    {
        super(builder);
    }

    public static <B extends SkillBuilder<B>> SkillBuilder<?> createSpecialArt(Function<B, ? extends Skill> constructor)
    {
        return Skill.createBuilder(constructor).setCategory(BattleArtsSkillCategories.SPECIAL_ART);
    }
}
