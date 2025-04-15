package net.forixaim.bs_api.tests;

import net.forixaim.bs_api.BattleArtsAPI;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.Armatures;

@Mod.EventBusSubscriber(modid = BattleArtsAPI.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DummyAnimations
{
	public static AnimationManager.AnimationAccessor<StaticAnimation> T_POSE;

	@SubscribeEvent
	public static void register(AnimationManager.AnimationRegistryEvent event)
	{
		event.newBuilder(BattleArtsAPI.MOD_ID, DummyAnimations::build);
	}

	public static void build(AnimationManager.AnimationBuilder builder)
	{
		T_POSE = builder.nextAccessor("dummy/tpose", access -> new StaticAnimation(
				true, access, Armatures.BIPED
		));
	}
}
