package net.forixaim.battle_arts_api.mixin;


import net.forixaim.battle_arts_api.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@Mixin(Entity.class)
public abstract class MixinEntity
{
	@Unique
	private final Entity battle_arts$entity = (Entity) (Object)this;

	@Inject(method = "walkingStepSound", at = @At("HEAD"), cancellable = true)
	public void step(BlockPos pPos, BlockState pState, CallbackInfo ci)
	{
		if (Config.allowSoundOverrides && battle_arts$entity instanceof Player pl && EpicFightCapabilities.getEntityPatch(pl, LivingEntityPatch.class) instanceof PlayerPatch<?> playerPatch && playerPatch.isEpicFightMode())
			ci.cancel();
	}
}
