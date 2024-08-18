package net.forixaim.bs_api.events;

import com.google.common.collect.Lists;
import net.forixaim.bs_api.proficiencies.Proficiency;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;
import yesman.epicfight.skill.Skill;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProficiencyRegistryEvent extends Event implements IModBusEvent {
	private final List<ModRegistryWorker> modRegisterWorkers = Lists.newArrayList();
	
	public ModRegistryWorker createRegistryWorker(String ModID) {
		ModRegistryWorker modRegisterWorker = new ModRegistryWorker(ModID);
		this.modRegisterWorkers.add(modRegisterWorker);
		
		return modRegisterWorker;
	}
	
	public Set<String> getNamespaces() {
		return this.modRegisterWorkers.stream().map((worker) -> worker.ModID).collect(Collectors.toSet());
	}
	
	public List<Proficiency> getAllProficiencies() {
		List<Proficiency> skills = Lists.newArrayList();
		
		this.modRegisterWorkers.forEach((registryWorker) -> {
			skills.addAll(registryWorker.modProficiencies);
		});
		
		return skills;
	}
	
	public static class ModRegistryWorker {
		private final String ModID;
		private final List<Proficiency> modProficiencies = Lists.newArrayList();
		
		private ModRegistryWorker(String ModID) {
			this.ModID = ModID;
		}
		
		public <S extends Proficiency, B extends Proficiency.Builder<S>> S build(String name, Function<B, S> constructor, B builder) {
			final ResourceLocation registryName = new ResourceLocation(this.ModID, name);
			builder.setRegistryName(registryName);
			
			final S skill = constructor.apply(builder);
			
			this.modProficiencies.add(skill);
			
			return skill;
		}
	}
}