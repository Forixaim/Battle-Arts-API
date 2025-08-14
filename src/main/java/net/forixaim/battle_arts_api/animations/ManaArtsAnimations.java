package net.forixaim.battle_arts_api.animations;

import net.forixaim.battle_arts_api.BattleArtsAPI;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.ActionAnimation;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Armatures;

@Mod.EventBusSubscriber(modid = BattleArtsAPI.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ManaArtsAnimations
{
	public static AnimationManager.AnimationAccessor<StaticAnimation> CHARGE_1;
	public static AnimationManager.AnimationAccessor<ActionAnimation> CHARGE_RELEASE_1;

	@SubscribeEvent
	public static void register(AnimationManager.AnimationRegistryEvent event)
	{
		event.newBuilder(BattleArtsAPI.MOD_ID, ManaArtsAnimations::build);
	}

	public static void build(AnimationManager.AnimationBuilder builder)
	{
		CHARGE_1 = builder.nextAccessor("mana_arts/charge1", access ->
				new StaticAnimation(0.3f,true, access, Armatures.BIPED));
		CHARGE_RELEASE_1 = builder.nextAccessor("mana_arts/charge_release1", access ->
				new ActionAnimation(0.1f, access, Armatures.BIPED)
						.addProperty(AnimationProperty.ActionAnimationProperty.STOP_MOVEMENT, true)
						.addState(EntityState.MOVEMENT_LOCKED, true));
	}
}
