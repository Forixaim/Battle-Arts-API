package net.forixaim.bs_api.proficiencies;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import net.forixaim.bs_api.BattleArtsAPI;
import net.forixaim.bs_api.events.ProficiencyRegistryEvent;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.registries.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class ProficiencyManager extends SimpleJsonResourceReloadListener
{
	public static final ResourceKey<Registry<Proficiency>> PROFICIENCY_REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(BattleArtsAPI.MOD_ID, "proficiency"));
	public static final List<Proficiency> REGISTERED_PROFICIENCIES = Lists.newArrayList();
	private static final Gson GSON = new GsonBuilder().create();

	public ProficiencyManager(Gson pGson, String pDirectory)
	{
		super(GSON, "battle_arts_proficiency_parameters");
	}

	public static void createProficiencyRegistry(NewRegistryEvent event) {
		event.create(RegistryBuilder.of(new ResourceLocation(BattleArtsAPI.MOD_ID, "proficiency")).addCallback(ProficiencyCallbacks.INSTANCE));
	}

	public static List<Proficiency> assignNewProficiencies()
	{
		return ImmutableList.copyOf(REGISTERED_PROFICIENCIES);
	}

	public static List<ResourceLocation> getRegisteredProficiencies()
	{
		List<ResourceLocation> list = Lists.newArrayList();
		REGISTERED_PROFICIENCIES.forEach((proficiency) -> list.add(proficiency.getIdentifier()));
		return list;
	}

	public static void registerProficiencies(RegisterEvent event)
	{
		if (event.getRegistryKey().equals(PROFICIENCY_REGISTRY_KEY))
		{
			final ProficiencyRegistryEvent proficiencyRegistryEvent = new ProficiencyRegistryEvent();
			ModLoader.get().postEvent(proficiencyRegistryEvent);

			event.register(PROFICIENCY_REGISTRY_KEY, registerHelper ->
			{
				proficiencyRegistryEvent.getAllProficiencies().forEach(
						proficiency ->
						{
							registerHelper.register(proficiency.getIdentifier(), proficiency);
							REGISTERED_PROFICIENCIES.add(proficiency);
						}
				);
			});
		}
	}

	public static IForgeRegistry<Proficiency> getProficiencyRegistry()
	{
		return RegistryManager.ACTIVE.getRegistry(PROFICIENCY_REGISTRY_KEY);
	}

	@Override
	public void apply(@NotNull Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller)
	{
		LogUtils.getLogger().debug("Nothing here...");
	}

	private static class ProficiencyCallbacks implements IForgeRegistry.BakeCallback<Proficiency>, IForgeRegistry.CreateCallback<Proficiency>, IForgeRegistry.ClearCallback<Proficiency>
	{
		private static final ResourceLocation PROFICIENCIES = new ResourceLocation(BattleArtsAPI.MOD_ID, "proficiency");
		public static final ProficiencyCallbacks INSTANCE = new ProficiencyCallbacks();
		@Override
		@SuppressWarnings("unchecked")
		public void onBake(IForgeRegistryInternal<Proficiency> iForgeRegistryInternal, RegistryManager registryManager)
		{
			LogUtils.getLogger().debug("Baked you a cookie.");
		}

		@Override
		public void onClear(IForgeRegistryInternal<Proficiency> iForgeRegistryInternal, RegistryManager registryManager)
		{
			iForgeRegistryInternal.getSlaveMap(PROFICIENCIES, Map.class).clear();
		}

		@Override
		public void onCreate(IForgeRegistryInternal<Proficiency> iForgeRegistryInternal, RegistryManager registryManager)
		{
			iForgeRegistryInternal.setSlaveMap(PROFICIENCIES, Maps.newHashMap());
		}
	}
}
