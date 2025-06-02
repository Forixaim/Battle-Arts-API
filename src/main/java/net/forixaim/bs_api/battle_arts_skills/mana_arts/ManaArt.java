package net.forixaim.bs_api.battle_arts_skills.mana_arts;

import net.forixaim.bs_api.battle_arts_skills.BattleArtsSkillCategories;
import net.minecraft.network.FriendlyByteBuf;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillBuilder;
import yesman.epicfight.skill.SkillContainer;

public abstract class ManaArt extends Skill
{

    public static SkillBuilder<? extends Skill> createManaArtBuilder()
    {
        return Skill.createBuilder().setCategory(BattleArtsSkillCategories.MANA_ART);
    }


    public ManaArt(SkillBuilder<? extends Skill> builder)
    {
        super(builder);
    }

    @Override
    public void executeOnServer(SkillContainer container, FriendlyByteBuf args)
    {
        super.executeOnServer(container, args);
    }
}
