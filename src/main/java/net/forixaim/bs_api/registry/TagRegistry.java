package net.forixaim.bs_api.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class TagRegistry
{
    public static class EntityTags
    {
        public static final TagKey<EntityType<?>> TARGET_DUMMY = createEntityTag("target_dummy");
        public static final TagKey<EntityType<?>> EXECUTION_IMMUNE = createEntityTag("execution_immune");

        private static TagKey<EntityType<?>> createEntityTag(String name)
        {
            return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("battle_arts", name));
        }
    }
}
