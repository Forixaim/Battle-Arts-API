package net.forixaim.battle_arts_api;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

/**
 * This config does fucking nothing.
 * @author Forixaim
 */
@Mod.EventBusSubscriber(modid = BattleArtsAPI.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec.BooleanValue RANDOM_CRITICAL_HITS = BUILDER.comment("Enable random critical hits").define("randomCriticalHits", true);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean randomCriticalHits;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        randomCriticalHits = RANDOM_CRITICAL_HITS.get();
    }
}
