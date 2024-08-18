package net.forixaim.bs_api.proficiencies;

import net.forixaim.bs_api.BattleArtsAPI;
import net.forixaim.bs_api.events.ProficiencyRegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BattleArtsAPI.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Proficiencies
{
	public static Proficiency SWORD;

	@SubscribeEvent
	public static void RegisterProficiencies(ProficiencyRegistryEvent event)
	{
		ProficiencyRegistryEvent.ModRegistryWorker registryWorker = event.createRegistryWorker(BattleArtsAPI.MOD_ID);

		SWORD = registryWorker.build("test_swords", Proficiency::new, Proficiency.createProficiencyBuilder());
	}
}
