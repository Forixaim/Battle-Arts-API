package net.forixaim.bs_api.mixin;

import net.forixaim.bs_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.bs_api.battle_arts_skills.battle_style.BattleStyle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;

@Mixin(WeaponInnateSkill.class)
public class MixinWeaponInnateSkill
{
    @Unique
    private WeaponInnateSkill battleArtsAPI$self = (WeaponInnateSkill) (Object)this;
    @Inject(method = "canExecute", at = @At("RETURN"), remap = false, cancellable = true)
    public void canExec(SkillContainer container, CallbackInfoReturnable<Boolean> cir)
    {
        if (!container.getExecutor().isLogicalClient() && container.getExecutor().getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getSkill() instanceof BattleStyle bs && bs.getUnarmedInnateSkill() == battleArtsAPI$self)
        {
            cir.setReturnValue(true);
        }
    }
}
