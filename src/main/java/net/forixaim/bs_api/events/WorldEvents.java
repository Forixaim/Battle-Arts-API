package net.forixaim.bs_api.events;

import net.forixaim.bs_api.BattleArtsAPI;
import net.forixaim.bs_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.bs_api.battle_arts_skills.battle_style.BattleStyle;
import net.forixaim.bs_api.capabilities.ProficiencyCapabilityProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;

@Mod.EventBusSubscriber(modid = BattleArtsAPI.MOD_ID)
public class WorldEvents
{
	@SubscribeEvent
	public static void PlayerKill(LivingDeathEvent event)
	{
		if (event.getSource() instanceof EpicFightDamageSource epicFightDamageSource)
		{
			if (epicFightDamageSource.getEntity() instanceof Player player)
			{
				PlayerPatch<?> playerPatch = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);
				Skill skill = playerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getSkill();
				player.getCapability(ProficiencyCapabilityProvider.PROFICIENCY_CAPABILITY).ifPresent(proficiencyCapability ->
				{
					proficiencyCapability.getProficiencies().forEach(proficiency ->
					{
						if (skill instanceof BattleStyle battleStyle)
						{
							if (battleStyle.checkProficiency(proficiency.getContainingProficiency()))
							{
								proficiency.addProficiency(battleStyle.getProficiencyXpPerKill());
							}
						}
					});
				});
			}
		}
	}
}
