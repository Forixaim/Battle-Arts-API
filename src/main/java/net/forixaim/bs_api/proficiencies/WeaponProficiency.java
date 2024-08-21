package net.forixaim.bs_api.proficiencies;

import com.google.common.collect.Lists;
import net.forixaim.bs_api.battle_arts_skills.battle_style.BattleStyle;
import net.minecraft.resources.ResourceLocation;
import yesman.epicfight.world.capabilities.item.WeaponCategory;

import java.util.Arrays;
import java.util.List;

public class WeaponProficiency extends Proficiency
{
	private final List<WeaponCategory> weaponCategories;

	public WeaponProficiency(Builder builder)
	{
		super(builder);
		weaponCategories = builder.categories;
	}

	public static Builder createWeaponProficiencyBuilder()
	{
		return new Builder();
	}

	public void addModdedCategories(WeaponCategory category)
	{
		weaponCategories.add(category);
	}

	public boolean categoryMatch(WeaponCategory weaponCategory)
	{
		return weaponCategories.contains(weaponCategory);
	}

	public static class Builder extends Proficiency.Builder<WeaponProficiency>
	{
		public List<WeaponCategory> categories;

		public Builder()
		{
			super();
			categories = Lists.newArrayList();
		}

		public Builder addWeaponCategory(WeaponCategory... weaponCategory)
		{
			categories.addAll(Arrays.stream(weaponCategory).toList());
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
