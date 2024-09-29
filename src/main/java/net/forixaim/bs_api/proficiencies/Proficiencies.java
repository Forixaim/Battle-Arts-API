package net.forixaim.bs_api.proficiencies;

import net.forixaim.bs_api.BattleArtsAPI;
import net.forixaim.bs_api.events.ProficiencyRegistryEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = BattleArtsAPI.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Proficiencies
{
	public static Proficiency SWORDS;
	public static Proficiency POLEARMS;
	public static Proficiency AXES;
	public static Proficiency BOWS;
	public static Proficiency MACES;
	public static Proficiency DAGGERS;
	public static Proficiency BRAWLING;
	public static Proficiency FIREARMS;
	public static Proficiency REASON;
	public static Proficiency DESIRE;
	public static Proficiency FAITH;
	public static Proficiency RIDING;
	public static Proficiency AUTHORITY;
	public static Proficiency ARMOR;
	public static Proficiency WEIGHT;
	public static Proficiency FLYING;


	@SubscribeEvent
	public static void RegisterProficiencies(ProficiencyRegistryEvent event)
	{
		ProficiencyRegistryEvent.ModRegistryWorker registryWorker = event.createRegistryWorker(BattleArtsAPI.MOD_ID);

		SWORDS = registryWorker.build("swords", WeaponProficiency::new, WeaponProficiency.createWeaponProficiencyBuilder()
				.addWeaponCategory(
						CapabilityItem.WeaponCategories.SWORD,
						CapabilityItem.WeaponCategories.LONGSWORD,
						CapabilityItem.WeaponCategories.GREATSWORD,
						CapabilityItem.WeaponCategories.TACHI,
						CapabilityItem.WeaponCategories.UCHIGATANA
				));
		POLEARMS = registryWorker.build("polearms", WeaponProficiency::new, WeaponProficiency.createWeaponProficiencyBuilder()
				.addWeaponCategory(
						CapabilityItem.WeaponCategories.SPEAR,
						CapabilityItem.WeaponCategories.TRIDENT
				));
		AXES = registryWorker.build("axes", WeaponProficiency::new, WeaponProficiency.createWeaponProficiencyBuilder()
				.addWeaponCategory(
						CapabilityItem.WeaponCategories.AXE
				));
		BOWS = registryWorker.build("bows", WeaponProficiency::new, WeaponProficiency.createWeaponProficiencyBuilder()
				.addWeaponCategory(
						CapabilityItem.WeaponCategories.RANGED
				));
		DAGGERS = registryWorker.build("daggers", WeaponProficiency::new, WeaponProficiency.createWeaponProficiencyBuilder()
				.addWeaponCategory(
						CapabilityItem.WeaponCategories.DAGGER
				));
		BRAWLING = registryWorker.build("brawling", WeaponProficiency::new, WeaponProficiency.createWeaponProficiencyBuilder()
				.addWeaponCategory(
						CapabilityItem.WeaponCategories.FIST
				));
		RIDING = registryWorker.build("riding", SpecialPredicateProficiency::new, SpecialPredicateProficiency.createSpecialPredicateProficiencyBuilder().setPredicate(
				playerPatch -> {
					if (playerPatch.getOriginal().isPassenger())
					{
						Entity entity = playerPatch.getOriginal().getVehicle();
						return entity instanceof PlayerRideableJumping rideable && rideable.canJump();
					}
					return false;
				}
		));

		FLYING = registryWorker.build("flying", SpecialPredicateProficiency::new, SpecialPredicateProficiency.createSpecialPredicateProficiencyBuilder().setPredicate(
				playerPatch -> playerPatch.getOriginal().isFallFlying()
		));

		WEIGHT = registryWorker.build("weight", SpecialPredicateProficiency::new, SpecialPredicateProficiency.createSpecialPredicateProficiencyBuilder()
				.setPredicate(
						(playerPatch) -> Objects.requireNonNull(playerPatch.getOriginal().getAttribute(EpicFightAttributes.WEIGHT.get())).getValue() > 50.0
				));
	}
}
