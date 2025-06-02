package net.forixaim.bs_api.registry;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import net.forixaim.bs_api.BattleArtsAPI;
import net.forixaim.bs_api.battle_arts_skills.BattleArtsSkillCategories;
import net.forixaim.bs_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.bs_api.battle_arts_skills.battle_style.BattleStyle;
import net.forixaim.bs_api.battle_arts_skills.battle_style.BattleStyleCategories;
import net.forixaim.bs_api.battle_arts_skills.battle_style.starting.Traveler;
import net.forixaim.bs_api.battle_arts_skills.mana_arts.Charge;
import net.forixaim.bs_api.battle_arts_skills.mana_arts.ManaArt;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.forgeevent.SkillBuildEvent;
import yesman.epicfight.skill.Skill;

@Mod.EventBusSubscriber(modid = BattleArtsAPI.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BattleStyleRegistry
{
	public static Skill TRAVELER;
	public static Skill MANA_CHARGE;

	@SubscribeEvent
	public static void RegisterSkills(SkillBuildEvent event)
	{
		SkillBuildEvent.ModRegistryWorker registryWorker = event.createRegistryWorker(BattleArtsAPI.MOD_ID);
		if (ModList.get().isLoaded(IronsSpellbooks.MODID))
			MANA_CHARGE = registryWorker.build("mana_charge", Charge::new, Skill.createBuilder().setCategory(BattleArtsSkillCategories.MANA_ART).setActivateType(Skill.ActivateType.CHARGING));
		TRAVELER = registryWorker.build("traveler", Traveler::new, BattleStyle.createBattleStyleBuilder().setBattleStyleCategory(BattleStyleCategories.STARTING).setResource(Skill.Resource.NONE));
	}
}
