package net.forixaim.bs_api.mixin;


import net.forixaim.bs_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.bs_api.battle_arts_skills.battle_style.BattleStyle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

@Mixin(value = PlayerPatch.class, remap = false)
public class MixinPlayerPatch
{
	@Unique
	private PlayerPatch<?> battleArtsAPI$inst = (PlayerPatch<?>) (Object) this;

	@Inject(method = "toggleMode", at = @At("RETURN"), remap = false)
	public void toggleMode(CallbackInfo ci)
	{
		if (battleArtsAPI$inst instanceof ServerPlayerPatch serverPlayerPatch)
		{
			if (!battleArtsAPI$inst.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).isEmpty() && battleArtsAPI$inst.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getSkill() instanceof BattleStyle battleStyle)
			{
				if (!battleStyle.getUnarmedBattleMotions().isEmpty() && battleArtsAPI$inst.isBattleMode())
					serverPlayerPatch.modifyLivingMotionByCurrentItem();
				else if (!battleStyle.getUnarmedLivingMotions().isEmpty() && !battleArtsAPI$inst.isBattleMode())
					serverPlayerPatch.modifyLivingMotionByCurrentItem();

				//adding the battle mode transition animations will be done in ExCap
			}
		}
	}
}
