package net.forixaim.bs_api.proficiencies;

import net.minecraft.network.chat.Component;

public enum ProficiencyRank
{
	E(Component.translatable("rank.bs_api.e"), 0, "E"),
	E_PLUS(Component.translatable("rank.bs_api.e_plus"), 1, "E+"),
	D_MINUS(Component.translatable("rank.bs_api.d_minus"), 2, "D-"),
	D(Component.translatable("rank.bs_api.d"), 3, "D"),
	D_PLUS(Component.translatable("rank.bs_api.d_plus"), 4, "D+"),
	C_MINUS(Component.translatable("rank.bs_api.c_minus"), 5, "C-"),
	C(Component.translatable("rank.bs_api.c"), 6, "C"),
	C_PLUS(Component.translatable("rank.bs_api.c_plus"), 7, "C+"),
	B_MINUS(Component.translatable("rank.bs_api.b_minus"), 8, "B-"),
	B(Component.translatable("rank.bs_api.b"), 9, "B"),
	B_PLUS(Component.translatable("rank.bs_api.b_plus"), 10, "B+"),
	A_MINUS(Component.translatable("rank.bs_api.a_minus"), 11, "A-"),
	A(Component.translatable("rank.bs_api.a"), 12, "A"),
	A_PLUS(Component.translatable("rank.bs_api.a_plus"), 13, "A+"),
	S_MINUS(Component.translatable("rank.bs_api.s_minus"), 14, "S-"),
	S(Component.translatable("rank.bs_api.s"), 15, "S"),
	S_PLUS(Component.translatable("rank.bs_api.s_plus"), 16, "S+"),
	EX_MINUS(Component.translatable("rank.bs_api.ex_minus"), 17, "EX-"),
	EX(Component.translatable("rank.bs_api.ex"), 18, "EX"),
	EX_PLUS(Component.translatable("rank.bs_api.ex_plus"), 19, "EX+"),
	GX(Component.translatable("rank.bs_api.gx"), 20, "GX"),
	CX(Component.translatable("rank.bs_api.cx"), 21, "CX"),
	;

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
