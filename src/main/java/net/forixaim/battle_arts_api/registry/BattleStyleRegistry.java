package net.forixaim.battle_arts_api.registry;

import net.forixaim.battle_arts_api.BattleArtsAPI;
import net.forixaim.battle_arts_api.battle_arts_skills.active.combat_arts.CombatArt;
import net.forixaim.battle_arts_api.battle_arts_skills.active.combat_arts.ExampleCombatArt;
import net.forixaim.battle_arts_api.battle_arts_skills.battle_style.BattleStyle;
import net.forixaim.battle_arts_api.battle_arts_skills.battle_style.BattleStyleCategories;
import net.forixaim.battle_arts_api.battle_arts_skills.battle_style.starting.Traveler;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.forgeevent.SkillBuildEvent;
import yesman.epicfight.skill.Skill;

@Mod.EventBusSubscriber(modid = BattleArtsAPI.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BattleStyleRegistry
{
	public static Skill TRAVELER;
    public static Skill EXAMPLE_COMBAT_ART;

	@SubscribeEvent
	public static void RegisterSkills(SkillBuildEvent event)
	{
		SkillBuildEvent.ModRegistryWorker registryWorker = event.createRegistryWorker(BattleArtsAPI.MOD_ID);
		TRAVELER = registryWorker.build("traveler", Traveler::new, BattleStyle.createBattleStyleBuilder().setBattleStyleCategory(BattleStyleCategories.STARTING).setResource(Skill.Resource.NONE));
	    EXAMPLE_COMBAT_ART = registryWorker.build("example_combat_art", ExampleCombatArt::new, CombatArt.createCombatArt().setResource(Skill.Resource.NONE));
    }
}
