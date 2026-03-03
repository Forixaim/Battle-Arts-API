package net.forixaim.battle_arts_api;


import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * This config sets up super meter positioning and sound overrides.
 * @author Forixaim
 */
@EventBusSubscriber(modid = BattleArtsAPI.MOD_ID)
public class Config
{


    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec.BooleanValue ALLOW_SOUND_OVERRIDES = BUILDER.comment("Allows Battle Arts API to override footsteps and replace it with keyframed ones (will break footsteps) [Not compatible with Prescence Footsteps]").define("allowSoundOverrides", false);


    public static final ModConfigSpec.DoubleValue SUPER_METER_POSITION_X = BUILDER.comment("Super Meter X Position").defineInRange("superMeterPositionX", -200.0, -10000, 10000);
    public static final ModConfigSpec.DoubleValue SUPER_METER_POSITION_Y = BUILDER.comment("Super Meter Y Position").defineInRange("superMeterPositionY", 0.0, -10000, 10000);
    public static final ModConfigSpec.DoubleValue SUPER_METER_SCALE_Y = BUILDER.comment("Super Meter Y Scale").defineInRange("superMeterScaleY", 1, Double.MIN_VALUE, Double.MAX_VALUE);
    public static final ModConfigSpec.DoubleValue SUPER_METER_SCALE_X = BUILDER.comment("Super Meter X Scale").defineInRange("superMeterScaleX", 1, Double.MIN_VALUE, Double.MAX_VALUE);


    static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean allowSoundOverrides;
    public static double superMeterPositionX;
    public static double superMeterPositionY;
    public static double superMeterScaleX;
    public static double superMeterScaleY;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        allowSoundOverrides = ALLOW_SOUND_OVERRIDES.get();

        superMeterPositionX = SUPER_METER_POSITION_X.get();
        superMeterPositionY = SUPER_METER_POSITION_Y.get();
        superMeterScaleX = SUPER_METER_SCALE_X.get();
        superMeterScaleY = SUPER_METER_SCALE_Y.get();
    }
}
