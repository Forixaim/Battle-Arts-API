package net.forixaim.battle_arts_api.registry;

import net.forixaim.battle_arts_api.BattleArtsAPI;
import net.forixaim.battle_arts_api.data.DamageAttribute;
import net.minecraft.nbt.CompoundTag;
import yesman.epicfight.registry.deferred.CustomDataRegister;
import yesman.epicfight.registry.deferred.holders.DeferredCustomData;
import yesman.epicfight.world.capabilities.item.custom.CustomData;

public class BattleArtsCustomData
{
    public static final CustomDataRegister REGISTRY = CustomDataRegister.createWeapon(BattleArtsAPI.MOD_ID);

    public static final DeferredCustomData<CustomData<DamageAttribute>> SLASH_MODIFIER = REGISTRY.registerCustomData("slash_modifier",
            () -> CustomData.createDeserializable(DamageAttribute.DEFAULT, tag ->
            {
                if (tag instanceof CompoundTag compoundTag)
                {
                    return DamageAttribute.deserialize(compoundTag);
                }
                return DamageAttribute.DEFAULT;
            }));
}
