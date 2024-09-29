package net.forixaim.bs_api.mixin;



import net.forixaim.bs_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.bs_api.battle_arts_skills.battle_style.BattleStyle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.data.reloader.SkillManager;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.network.server.SPDatapackSyncSkill;

@Mixin(value = SkillManager.class, remap = false)
public abstract class MixinSkillManager
{
	@Inject(method = "processServerPacket", at = @At("RETURN"), remap = false)
	private static void ef_bs$processServerPacket(SPDatapackSyncSkill packet, final CallbackInfo ci)
	{
		LocalPlayerPatch localPlayerPatch = ClientEngine.getInstance().getPlayerPatch();

		if (localPlayerPatch != null)
		{
			if (!localPlayerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).isEmpty() && localPlayerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getSkill() instanceof BattleStyle battleStyle)
			{
				if (battleStyle.canModifyAttacks())
				{
					localPlayerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).sendExecuteRequest(localPlayerPatch, ClientEngine.getInstance().controllEngine);
				}
			}
		}
	}
}
