package net.forixaim.battle_arts_api.events;


import net.forixaim.battle_arts_api.BattleArtsAPI;
import net.forixaim.battle_arts_api.client.InputHandler;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;


@EventBusSubscriber(modid = BattleArtsAPI.MOD_ID, value = Dist.CLIENT)
public class ClientNeoEvents
{
    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event)
    {
        if (Minecraft.getInstance().getOverlay() == null && Minecraft.getInstance().screen == null) {
            InputHandler.handleKeybinds();
        }
    }


}
