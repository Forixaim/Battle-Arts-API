package net.forixaim.battle_arts_api.mixin;

import com.mojang.logging.LogUtils;
import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.battle_arts_api.battle_arts_skills.battle_style.BattleStyle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.guard.GuardSkill;

import java.util.List;

@Mixin(GuardSkill.class)
public class MixinGuard {
    @Inject(method = "canExecute", at = @At("HEAD"), remap = false, cancellable = true)
    public void guard(SkillContainer container, CallbackInfoReturnable<Boolean> cir)
    {
        LogUtils.getLogger().debug("Animations: {}", container.getExecutor().getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getSkill() instanceof BattleStyle bs);

        if (container.getExecutor().getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getSkill() instanceof BattleStyle bs)
        {
            List<AnimationManager.AnimationAccessor<? extends StaticAnimation>> guards = bs.getGuardMaps().get((GuardSkill) (Object)this).get(GuardSkill.BlockType.GUARD);
            LogUtils.getLogger().debug("Animations: {}", guards);

            if (guards != null && !guards.isEmpty())
            {
                cir.setReturnValue(true);
            }
        }
    }
}
