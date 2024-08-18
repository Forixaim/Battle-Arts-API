package net.forixaim.bs_api;

import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class AnimationHelpers
{
	public static boolean isInAir(LivingEntityPatch<?> entityPatch)
	{
		Vec3 playerPos = entityPatch.getOriginal().getPosition(0f);
		Vec3 scanPos = playerPos.subtract(0, 0.2f, 0);
		HitResult hitResult = entityPatch.getOriginal().level().clip(new ClipContext(playerPos, scanPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entityPatch.getOriginal()));
		if (hitResult.getType() == HitResult.Type.MISS)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
