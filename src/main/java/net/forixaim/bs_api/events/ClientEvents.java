package net.forixaim.bs_api.events;


import net.forixaim.bs_api.BattleArtsAPI;
import net.forixaim.bs_api.client.KeyBinds;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BattleArtsAPI.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents
{
	@SubscribeEvent
	public static void onKeyRegister(RegisterKeyMappingsEvent event)
	{
		event.register(KeyBinds.USE_ART_1);
		event.register(KeyBinds.USE_TAUNT);
		event.register(KeyBinds.USE_BURST_ART);
		event.register(KeyBinds.USE_ULTIMATE_ART);
		event.register(KeyBinds.SHOW_BATTLE_STYLE_MENU);

	}


}
