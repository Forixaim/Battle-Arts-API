package net.forixaim.bs_api.proficiencies;

import net.forixaim.bs_api.battle_arts_skills.battle_style.BattleStyle;
import net.minecraft.resources.ResourceLocation;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import java.util.function.Function;

public class SpecialPredicateProficiency extends Proficiency
{
	protected Function<PlayerPatch<?>, Boolean> predicate;

	public SpecialPredicateProficiency(Builder builder)
	{
		super(builder);
		this.predicate = builder.predicate;
	}

	public boolean test(final PlayerPatch<?> patch)
	{
		return predicate.apply(patch);
	}

	public static Builder createSpecialPredicateProficiencyBuilder()
	{
		return new Builder();
	}

	public static class Builder extends Proficiency.Builder<SpecialPredicateProficiency>
	{
		protected Function<PlayerPatch<?>, Boolean> predicate;

		public Builder()
		{
			super();
		}

		public Builder setPredicate(final Function<PlayerPatch<?>, Boolean> predicate)
		{
			this.predicate = predicate;
			return this;
		}

		public Builder setRegistryName(final ResourceLocation identifier)
		{
			this.identifier = identifier;
			return this;
		}

		public Builder addXpSource(BattleStyle style)
		{
			xpSources.add(style);
			return this;
		}
	}
}
