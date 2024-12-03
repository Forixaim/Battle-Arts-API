package net.forixaim.bs_api.proficiencies;

import net.minecraft.network.chat.Component;

public enum ProficiencyRank
{
	E(Component.translatable("rank.bs_api.e"), 0, "E"),
	D(Component.translatable("rank.bs_api.d"), 3, "D"),
	C(Component.translatable("rank.bs_api.c"), 6, "C"),
	B(Component.translatable("rank.bs_api.b"), 9, "B"),
	A(Component.translatable("rank.bs_api.a"), 12, "A"),
	S(Component.translatable("rank.bs_api.s"), 15, "S"),
	EX(Component.translatable("rank.bs_api.ex"), 18, "EX");

	final Component displayName;
	final int id;
	final String debugIdentifier;

	public static ProficiencyRank getRankFromId(final int id)
	{
		if (id >= 0 && id < ProficiencyRank.values().length)
			return ProficiencyRank.values()[id];
		return null;
	}

	ProficiencyRank(Component displayName, int id, String debugIdentifier)
	{
		this.displayName = displayName;
		this.id = id;
		this.debugIdentifier = debugIdentifier;
	}

	public int getId()
	{
		return id;
	}

	public Component getDisplayName()
	{
		return this.displayName;
	}

	public String getDebugIdentifier()
	{
		return this.debugIdentifier;
	}
}
