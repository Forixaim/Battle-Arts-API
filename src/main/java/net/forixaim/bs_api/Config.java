package net.forixaim.bs_api;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
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
