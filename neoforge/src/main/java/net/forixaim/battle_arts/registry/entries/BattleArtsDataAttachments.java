package net.forixaim.battle_arts.registry.entries;

import net.forixaim.battle_arts.BattleArts;
import net.forixaim.battle_arts.data_attachment.BattleArtsEntityNeoForged;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class BattleArtsDataAttachments
{
    private BattleArtsDataAttachments() {}

    public static final DeferredRegister<AttachmentType<?>> REGISTRY = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, BattleArts.MOD_ID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<BattleArtsEntityNeoForged>> BATTLE_ARTS_ENTITY = REGISTRY.register("battle_arts_entity", () -> AttachmentType.serializable());
}
