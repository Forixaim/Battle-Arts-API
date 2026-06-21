package net.forixaim.battle_arts.data;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public interface BattleStyle
{
    Identifier id();
    String translationKey();
    String descriptionKey();
    default Component displayName()
    {
        return Component.translatable(translationKey());
    }
    default Component description()
    {
        return Component.translatable(descriptionKey());
    }
    Map<Holder<Attribute>, AttributeModifier> modifiers();

    default Map<Holder<Attribute>, AttributeModifier> loadDatapackAttributes(CompoundTag compound)
    {
        Map<Holder<Attribute>, AttributeModifier> result = new HashMap<>();
        for (String key : compound.keySet())
        {
            compound.getCompound(key).ifPresent(attributeTag -> {
                Optional<Holder.Reference<Attribute>> attribute = BuiltInRegistries.ATTRIBUTE.get(Identifier.parse(key));
                if (attribute.isPresent())
                {
                    AttributeModifier.Operation op = null;
                    Optional<String> opString = attributeTag.getString("operation");
                    if (opString.isPresent())
                    {
                        op = AttributeModifier.Operation.valueOf(opString.get());
                    }
                    Optional<Double> amountOptional = attributeTag.getDouble("amount");

                    if (op != null && amountOptional.isPresent())
                    {
                        AttributeModifier modifier = new AttributeModifier(id(), amountOptional.get(), op);
                        result.put(attribute.get(), modifier);
                    }
                }
            });
        }
        return result;
    }

}
