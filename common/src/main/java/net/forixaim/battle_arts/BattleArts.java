package net.forixaim.battle_arts;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;

/**
 * <p>This is the Battle Arts API used for almost every Battle Arts Suite of mods</p>
 * @author Forixaim
 */
public class BattleArts
{
    public static final String MOD_ID = "battle_arts";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static Identifier identifier(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
