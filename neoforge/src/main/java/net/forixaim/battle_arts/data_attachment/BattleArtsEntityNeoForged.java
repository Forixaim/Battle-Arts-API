package net.forixaim.battle_arts.data_attachment;

import net.forixaim.battle_arts.data.BattleArtsEntity;
import net.forixaim.battle_arts.data.BattleStyle;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

public class BattleArtsEntityNeoForged implements BattleArtsEntity, ValueIOSerializable
{
    @Override
    public BattleStyle battleStyle()
    {
        return null;
    }

    @Override
    public void serialize(ValueOutput output)
    {
        output.
    }

    @Override
    public void deserialize(ValueInput input)
    {

    }
}
