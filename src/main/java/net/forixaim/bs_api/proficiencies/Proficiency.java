package net.forixaim.bs_api.proficiencies;

import com.google.common.collect.Lists;
import net.forixaim.bs_api.battle_arts_skills.battle_style.BattleStyle;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class Proficiency
{
	private final ResourceLocation identifier;
	private final Component displayName;
	public Proficiency(Builder<? extends Proficiency> builder)
	{
		this.identifier = builder.identifier;
		displayName = Component.translatable("proficiencies." + this.identifier.getNamespace() + "." + this.identifier.getPath() + ".name");
	}

	public ResourceLocation getTextureLocation()
	{
		return new ResourceLocation(identifier.getNamespace(), "textures/proficiencies/" + this.identifier.getPath() + ".png");
	}

	public Component getDisplayName()
	{
		return displayName;
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
