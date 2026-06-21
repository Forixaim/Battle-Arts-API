package net.forixaim.battle_arts.data;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

import java.util.Map;

public record BattleStyleNeoForged(Identifier identifier) implements BattleStyle, ValueIOSerializable
{
    @Override
    public Identifier id()
    {
        return identifier;
    }

    @Override
    public String translationKey()
    {
        return identifier().getNamespace().concat(".").concat(identifier().getPath()).concat(".name");
    }

    @Override
    public String descriptionKey()
    {
        return identifier().getNamespace().concat(".").concat(identifier().getPath()).concat(".description");
    }

    @Override
    public Map<Holder<Attribute>, AttributeModifier> modifiers()
    {
        return Map.of();
    }

    @Override
    public void serialize(ValueOutput output)
    {
        output.putString("translation_key", translationKey());
        output.putString("description_key", descriptionKey());
        modifiers().forEach((attribute, modifier) -> {
            output.child("modifiers").child(attribute.getRegisteredName()).putDouble("amount", modifier.amount());
            output.child("modifiers").child(attribute.getRegisteredName()).putString("operation", modifier.operation().name());
        });
    }

    @Override
    public void deserialize(ValueInput input)
    {
        input.getString("translation_key");
        input.getString("description_key");
        input.child("modifiers").ifPresent(child -> {
            child.
        });
    }
}
