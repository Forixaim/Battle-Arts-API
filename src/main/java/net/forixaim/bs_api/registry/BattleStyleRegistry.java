package net.forixaim.bs_api.registry;

import net.forixaim.bs_api.BattleArtsAPI;
import net.forixaim.bs_api.battle_arts_skills.battle_style.BattleStyle;
import net.forixaim.bs_api.battle_arts_skills.battle_style.BattleStyleCategories;
import net.forixaim.bs_api.battle_arts_skills.battle_style.starting.Traveler;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.forgeevent.SkillBuildEvent;
import yesman.epicfight.skill.Skill;

@Mod.EventBusSubscriber(modid = BattleArtsAPI.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BattleStyleRegistry
{
	public static Skill TRAVELER;

	@SubscribeEvent
	public static void RegisterSkills(SkillBuildEvent event)
	{
		SkillBuildEvent.ModRegistryWorker registryWorker = event.createRegistryWorker(BattleArtsAPI.MOD_ID);

		TRAVELER = registryWorker.build("traveler", Traveler::new, BattleStyle.createBattleStyleBuilder().setBattleStyleCategory(BattleStyleCategories.STARTING));
	}
}
