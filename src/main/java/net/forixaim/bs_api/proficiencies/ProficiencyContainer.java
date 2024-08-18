package net.forixaim.bs_api.proficiencies;

public class ProficiencyContainer
{
	protected final Proficiency containingProficiency;
	private ProficiencyRank rank;
	private int currentXp, toNextLevel;

	public Proficiency getContainingProficiency()
	{
		return containingProficiency;
	}

	public Boolean contains(Proficiency proficiency)
	{
		return this.containingProficiency == proficiency;
	}

	public Boolean isEmpty()
	{
		return this.containingProficiency == null;
	}

	public int getCurrentXp()
	{
		return currentXp;
	}

	public ProficiencyRank getCurrentRank()
	{
		return this.rank;
	}

	public int getLevelThreshold()
	{
		return toNextLevel;
	}

	public ProficiencyContainer(final ProficiencyRank rank, final int currentXp, final Proficiency containingProficiency)
	{
		this.rank = rank;
		this.currentXp = currentXp;
		this.toNextLevel = updateXpThreshold();
		this.containingProficiency = containingProficiency;
	}

	private void levelUp()
	{
		currentXp -= toNextLevel;
		rank = ProficiencyRank.getRankFromId(rank.getId() + 1);
		toNextLevel = updateXpThreshold();
	}

	public void addProficiency(final int amount)
	{
		currentXp += amount;
		if (currentXp >= toNextLevel)
		{
			levelUp();
		}
	}

	private int updateXpThreshold()
	{
		return Math.round(100 * ((rank.getId() + 1) * (1 + rank.getId()) * 1.5f));
	}
}
