package net.forixaim.battle_arts_api.battle_arts_skills.special_arts;

import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillCategories;
import net.minecraft.network.FriendlyByteBuf;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillBuilder;
import yesman.epicfight.skill.SkillContainer;

public abstract class SpecialArt extends Skill
{

    public static SkillBuilder<? extends Skill> createManaArtBuilder()
    {
        return Skill.createBuilder().setCategory(BattleArtsSkillCategories.SPECIAL_ART);
    }


    public SpecialArt(SkillBuilder<? extends Skill> builder)
    {
        super(builder);
    }

    @Override
    public void executeOnServer(SkillContainer container, FriendlyByteBuf args)
    {
        super.executeOnServer(container, args);
    }
}
