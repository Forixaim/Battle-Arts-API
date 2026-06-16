package net.forixaim.battle_arts_api;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

/**
 * <p>This is the Battle Arts API used for almost every Battle Arts Suite of mods</p>
 * @author Forixaim
 */
@Mod(BattleArtsAPI.MOD_ID)
public class BattleArtsAPI
{
    public static final String MOD_ID = "battle_arts_api";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static Identifier identifier(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public BattleArtsAPI(IEventBus bus, ModContainer container) {
        registerEnums();
        registerRegistries(bus);
        container.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    public void registerEnums()
    {

    }

    public void registerRegistries(IEventBus modEventBus)
    {

    }
}
