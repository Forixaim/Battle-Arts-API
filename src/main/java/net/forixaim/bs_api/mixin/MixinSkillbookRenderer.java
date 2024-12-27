package net.forixaim.bs_api.mixin;

import net.forixaim.bs_api.battle_arts_skills.BattleArtsSkillCategories;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.client.renderer.patched.item.EpicFightItemProperties;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategory;
import yesman.epicfight.world.item.SkillBookItem;

@Mixin(value = EpicFightItemProperties.class, remap = false)
public class MixinSkillbookRenderer
{
    @Inject(method = "lambda$registerItemProperties$0", at = @At("RETURN"), remap = false, cancellable = true)
    private static void addBattleArtsCompatibleSkillbooks(ItemStack itemstack, ClientLevel level, LivingEntity entity, int i, CallbackInfoReturnable<Float> cir)
    {
        Skill skill = SkillBookItem.getContainSkill(itemstack);

        if (skill != null)
        {
            SkillCategory skillCategory = skill.getCategory();
            if (skillCategory == BattleArtsSkillCategories.BATTLE_STYLE)
            {
                cir.setReturnValue(8f);
            }
            if (skillCategory == BattleArtsSkillCategories.COMBAT_ART)
            {
                cir.setReturnValue(9f);
            }
            if (skillCategory == BattleArtsSkillCategories.BURST_ART)
            {
                cir.setReturnValue(10f);
            }
            if (skillCategory == BattleArtsSkillCategories.ULTIMATE_ART)
            {
                cir.setReturnValue(11f);
            }
        }
    }
}
