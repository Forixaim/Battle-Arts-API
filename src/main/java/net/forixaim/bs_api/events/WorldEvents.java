package net.forixaim.bs_api.events;

import net.forixaim.bs_api.BattleArtsAPI;
import net.forixaim.bs_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.bs_api.battle_arts_skills.battle_style.BattleStyle;
import net.forixaim.bs_api.capabilities.ProficiencyCapabilityProvider;
import net.forixaim.bs_api.network.NetworkHelpers;
import net.forixaim.bs_api.proficiencies.SpecialPredicateProficiency;
import net.forixaim.bs_api.proficiencies.WeaponProficiency;
import net.forixaim.bs_api.registry.BattleStyleRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.network.client.CPChangeSkill;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.WeaponCategory;

@Mod.EventBusSubscriber(modid = BattleArtsAPI.MOD_ID)
public class WorldEvents
{
	public static void onPlayerJoin(EntityJoinLevelEvent event)
	{
		if (event.getEntity() instanceof Player player)
		{
			PlayerPatch<?> playerPatch = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);
			SkillContainer battleStyleContainer = playerPatch.getSkillCapability().skillContainers[BattleArtsSkillSlots.BATTLE_STYLE.universalOrdinal()];
			if (battleStyleContainer.isEmpty())
			{
				battleStyleContainer.setSkill(BattleStyleRegistry.TRAVELER);
				NetworkHelpers.clientSendSkill(battleStyleContainer.getSlot(), BattleStyleRegistry.TRAVELER);
			}
		}
	}

	@SubscribeEvent
	public static void PlayerKill(LivingDeathEvent event)
	{
		if (event.getSource().is(DamageTypes.PLAYER_ATTACK))
		{
			if (event.getSource().getEntity() instanceof Player player)
			{
				PlayerPatch<?> playerPatch = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);
				Skill skill = playerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getSkill();
				WeaponCategory category = playerPatch.getHoldingItemCapability(InteractionHand.MAIN_HAND).getWeaponCategory();
				player.getCapability(ProficiencyCapabilityProvider.PROFICIENCY_CAPABILITY).ifPresent(proficiencyCapability ->
				{
					proficiencyCapability.getProficiencies().forEach(proficiency ->
					{
						int battleStyleModifier = 0;
						if (skill instanceof BattleStyle battleStyle)
						{
							if (battleStyle.checkProficiency(proficiency.getContainingProficiency()))
								battleStyleModifier = battleStyle.getProficiencyBonus();
						}
						if (proficiency.getContainingProficiency() instanceof WeaponProficiency weaponProficiency && weaponProficiency.categoryMatch(category))
						{
							proficiency.addProficiency(1 + battleStyleModifier);
						}
						if (proficiency.getContainingProficiency() instanceof SpecialPredicateProficiency sPP && sPP.test(playerPatch))
						{
							proficiency.addProficiency(1 + battleStyleModifier);
						}
					});
				});
			}
		}
	}
}
