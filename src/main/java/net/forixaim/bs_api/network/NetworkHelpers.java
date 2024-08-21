package net.forixaim.bs_api.network;

import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.network.client.CPChangeSkill;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillSlot;

public class NetworkHelpers
{
	public static void clientSendSkill(SkillSlot slot, Skill skill)
	{
		EpicFightNetworkManager.sendToServer(new CPChangeSkill(slot.universalOrdinal(), -1, skill.toString(), false));
	}
}
