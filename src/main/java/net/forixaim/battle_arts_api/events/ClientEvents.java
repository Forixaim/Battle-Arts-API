package net.forixaim.battle_arts_api.events;


import net.forixaim.battle_arts_api.BattleArtsAPI;
import net.forixaim.battle_arts_api.client.KeyBinds;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;


@EventBusSubscriber(modid = BattleArtsAPI.MOD_ID, value = Dist.CLIENT)
public class ClientEvents
{
	@SubscribeEvent
	public static void onKeyRegister(RegisterKeyMappingsEvent event)
	{
		event.register(KeyBinds.USE_ART_1);
		event.register(KeyBinds.USE_BURST_ART);
		event.register(KeyBinds.USE_ULTIMATE_ART);
		event.register(KeyBinds.USE_MANA_ART);
	}
}
