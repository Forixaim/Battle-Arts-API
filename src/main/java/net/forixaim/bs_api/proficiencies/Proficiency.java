package net.forixaim.bs_api.proficiencies;

import com.google.common.collect.Lists;
import net.forixaim.bs_api.battle_arts_skills.battle_style.BattleStyle;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class Proficiency
{
	private final ResourceLocation identifier;
	private ProficiencyRank rank;
	public Proficiency(Builder<? extends Proficiency> builder)
	{
		this.identifier = builder.identifier;
		this.rank = ProficiencyRank.E;
	}

	public ProficiencyRank getRank()
	{
		return rank;
	}

	private Proficiency(ResourceLocation identifier, ProficiencyRank rank, int xp, int xp_threshold)
	{
		this.identifier = identifier;
		this.rank = rank;
	}

	public static Proficiency loadProficiency(ResourceLocation identifier, ProficiencyRank rank, int xp, int xp_threshold)
	{
		return new Proficiency(identifier, rank, xp, xp_threshold);
	}

	public ResourceLocation getIdentifier()
	{
		return identifier;
	}

	public static class Builder<T extends Proficiency>
	{
		protected ResourceLocation identifier;
		protected List<BattleStyle> xpSources = Lists.newArrayList();

		public Builder<T> setRegistryName(final ResourceLocation identifier)
		{
			this.identifier = identifier;
			return this;
		}

		public Builder<T> addXpSource(BattleStyle style)
		{
			xpSources.add(style);
			return this;
		}
	}

	public static Builder<Proficiency> createProficiencyBuilder()
	{
		return new Builder<>();
	}
}
