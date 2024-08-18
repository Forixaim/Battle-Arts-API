package net.forixaim.bs_api.events;


import net.forixaim.bs_api.BattleArtsAPI;
import net.forixaim.bs_api.capabilities.ProficiencyCapabilityProvider;
import net.forixaim.bs_api.capabilities.ProficiencyCapability;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BattleArtsAPI.MOD_ID)
public class CapabilityEvent
{
	@SubscribeEvent
	public static void AttachEntityCapabilityEvent(final AttachCapabilitiesEvent<Entity> event)
	{
		if (event.getObject() instanceof Player player && !player.getCapability(ProficiencyCapabilityProvider.PROFICIENCY_CAPABILITY).isPresent())
		{
			event.addCapability(new ResourceLocation(BattleArtsAPI.MOD_ID, "battle_arts"), new ProficiencyCapabilityProvider());
		}
	}

	@SubscribeEvent
	public static void PlayerDeathBackup(PlayerEvent.Clone event)
	{
		if (event.isWasDeath())
		{
			event.getOriginal().getCapability(ProficiencyCapabilityProvider.PROFICIENCY_CAPABILITY).ifPresent(
					oldProficiencyCapability -> event.getOriginal().getCapability(ProficiencyCapabilityProvider.PROFICIENCY_CAPABILITY).ifPresent(
							newProficiencyCapability -> newProficiencyCapability.Copy(oldProficiencyCapability)
					)
			);
		}
	}

	@SubscribeEvent
	public static void RegisterCapabilities(final RegisterCapabilitiesEvent event)
	{
		event.register(ProficiencyCapability.class);
	}

}
