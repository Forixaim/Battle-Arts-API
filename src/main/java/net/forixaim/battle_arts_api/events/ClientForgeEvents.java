package net.forixaim.battle_arts_api.events;


import net.forixaim.battle_arts_api.BattleArtsAPI;
import net.forixaim.battle_arts_api.client.InputHandler;
import net.forixaim.battle_arts_api.client.KeyBinds;
import net.forixaim.battle_arts_api.client.particles.ChargeAura;
import net.forixaim.battle_arts_api.registry.ParticleRegistry;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BattleArtsAPI.MOD_ID, value = Dist.CLIENT)
public class ClientForgeEvents
{
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (Minecraft.getInstance().getOverlay() == null && Minecraft.getInstance().screen == null) {
            InputHandler.handleKeybinds();
        }
    }


}
