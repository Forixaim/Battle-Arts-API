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
    public static final ForgeConfigSpec.BooleanValue ALLOW_SOUND_OVERRIDES = BUILDER.comment("Allows Battle Arts API to override footsteps and replace it with keyframed ones (will break footsteps) [Not compatible with Prescence Footsteps]").define("allowSoundOverrides", false);


    public static final ForgeConfigSpec.DoubleValue SUPER_METER_POSITION_X = BUILDER.comment("Super Meter X Position").defineInRange("superMeterPositionX", -200.0, -10000, 10000);
    public static final ForgeConfigSpec.DoubleValue SUPER_METER_POSITION_Y = BUILDER.comment("Super Meter Y Position").defineInRange("superMeterPositionY", 0.0, -10000, 10000);
    public static final ForgeConfigSpec.DoubleValue SUPER_METER_SCALE_Y = BUILDER.comment("Super Meter Y Scale").defineInRange("superMeterScaleY", 1, Double.MIN_VALUE, Double.MAX_VALUE);
    public static final ForgeConfigSpec.DoubleValue SUPER_METER_SCALE_X = BUILDER.comment("Super Meter X Scale").defineInRange("superMeterScaleX", 1, Double.MIN_VALUE, Double.MAX_VALUE);


    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean randomCriticalHits;
    public static boolean allowSoundOverrides;
    public static double superMeterPositionX;
    public static double superMeterPositionY;
    public static double superMeterScaleX;
    public static double superMeterScaleY;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        allowSoundOverrides = ALLOW_SOUND_OVERRIDES.get();
        randomCriticalHits = RANDOM_CRITICAL_HITS.get();

        superMeterPositionX = SUPER_METER_POSITION_X.get();
        superMeterPositionY = SUPER_METER_POSITION_Y.get();
        superMeterScaleX = SUPER_METER_SCALE_X.get();
        superMeterScaleY = SUPER_METER_SCALE_Y.get();
    }
}
