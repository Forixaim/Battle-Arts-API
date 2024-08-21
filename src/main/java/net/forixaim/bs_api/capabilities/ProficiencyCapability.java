package net.forixaim.bs_api.capabilities;

import com.google.common.collect.Lists;
import net.forixaim.bs_api.proficiencies.Proficiency;
import net.forixaim.bs_api.proficiencies.ProficiencyContainer;
import net.forixaim.bs_api.proficiencies.ProficiencyManager;
import net.forixaim.bs_api.proficiencies.ProficiencyRank;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import yesman.epicfight.api.data.reloader.SkillManager;

import java.util.List;

public class ProficiencyCapability
{
	private List<ProficiencyContainer> proficiencies;

	public ProficiencyCapability()
	{
		proficiencies = Lists.newArrayList();
		for (Proficiency proficiency : ProficiencyManager.REGISTERED_PROFICIENCIES)
		{
			proficiencies.add(new ProficiencyContainer(ProficiencyRank.E, 0, proficiency));
		}
	}

	public void refreshProficiencies()
	{
		if (proficiencies != null)
		{
			proficiencies.clear();
			for (Proficiency proficiency : ProficiencyManager.REGISTERED_PROFICIENCIES)
			{
				proficiencies.add(new ProficiencyContainer(ProficiencyRank.E, 0, proficiency));
			}
		}
		else
		{
			proficiencies = Lists.newArrayList();
			for (Proficiency proficiency : ProficiencyManager.REGISTERED_PROFICIENCIES)
			{
				proficiencies.add(new ProficiencyContainer(ProficiencyRank.E, 0, proficiency));
			}		}
	}



	public CompoundTag toNBTData()
	{
		final CompoundTag tag = new CompoundTag();

		if (!proficiencies.isEmpty())
		{
			CompoundTag proficiencyTag = new CompoundTag();
			for (final ProficiencyContainer proficiencyContainer : proficiencies)
			{
				CompoundTag mainTag = new CompoundTag();
				mainTag.putString("identifier", proficiencyContainer.getContainingProficiency().getIdentifier().toString());
				mainTag.putInt("rank", proficiencyContainer.getCurrentRank().getId());
				mainTag.putInt("current_xp", proficiencyContainer.getCurrentXp());
				mainTag.putInt("to_next_level", proficiencyContainer.getLevelThreshold());
				proficiencyTag.put("proficiency:" + proficiencyContainer.getContainingProficiency().getIdentifier().toString(), mainTag);
			}
			tag.put("proficiencies", proficiencyTag);
		}
		return tag;
	}

	public List<ProficiencyContainer> getProficiencies()
	{
		return proficiencies;
	}

	public ProficiencyContainer getProficiency(Proficiency proficiency)
	{
		for (ProficiencyContainer proficiencyContainer : proficiencies)
		{
			if (proficiencyContainer.getContainingProficiency() == proficiency)
			{
				return proficiencyContainer;
			}
		}
		return null;
	}


	public void fromNBTData(final CompoundTag tag) throws NullPointerException
	{
		if (tag.contains("proficiencies"))
		{
			if (proficiencies != null)
				refreshProficiencies();
			else
				proficiencies = Lists.newArrayList();
			for (String key : tag.getAllKeys())
			{
				if (tag.get(key) instanceof CompoundTag proficiencyTag)
				{
					for (ProficiencyContainer proficiencyContainer : proficiencies)
					{
						if (proficiencyTag.get("proficiency:" + proficiencyContainer.getContainingProficiency().getIdentifier().toString()) instanceof CompoundTag mainTag)
						{
							ResourceLocation identifier = new ResourceLocation(mainTag.getString("identifier"));
							List<ResourceLocation> registeredProficiencies = ProficiencyManager.getRegisteredProficiencies();

							if (registeredProficiencies.contains(identifier))
							{
								Proficiency loadedProficiency = null;
								for (Proficiency proficiency : ProficiencyManager.REGISTERED_PROFICIENCIES)
								{
									if (proficiency.getIdentifier().equals(identifier))
										loadedProficiency = proficiency;
								}
								if (loadedProficiency == null)
								{
									throw new NullPointerException("Could not find proficiency with identifier " + identifier);
								}
								//Rewrites new proficiency data with saved one
								for (int index = 0; index < registeredProficiencies.size(); index++)
								{
									if (proficiencies.get(index).getContainingProficiency() == loadedProficiency)
									{
										proficiencies.set(index, new ProficiencyContainer(ProficiencyRank.getRankFromId(mainTag.getInt("rank")), mainTag.getInt("current_xp"), loadedProficiency));
									}
								}
							}

						}
					}

				}
			}
		}
	}

	public void Copy(final ProficiencyCapability oldProficiencyCapability)
	{
		this.proficiencies = oldProficiencyCapability.proficiencies;
	}
}
