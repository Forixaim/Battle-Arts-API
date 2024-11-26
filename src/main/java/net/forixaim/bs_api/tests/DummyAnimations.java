package net.forixaim.bs_api.tests;

import net.forixaim.bs_api.BattleArtsAPI;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.forgeevent.AnimationRegistryEvent;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.model.armature.HumanoidArmature;

@Mod.EventBusSubscriber(modid = BattleArtsAPI.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DummyAnimations
{
	public static StaticAnimation T_POSE;

	@SubscribeEvent
	public static void register(AnimationRegistryEvent event)
	{
		event.getRegistryMap().put(BattleArtsAPI.MOD_ID, DummyAnimations::build);
	}

	public static void build()
	{
		HumanoidArmature biped = Armatures.BIPED;

		T_POSE = new StaticAnimation(true, "dummy/tpose", biped);
	}
}
