package net.forixaim.bs_api.mixin.optional;

import com.yesman.epicskills.client.gui.screen.SkillTreeScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SkillTreeScreen.TreePage.class)
public interface TreePageAccessor {
    @Accessor(value = "pageLeft", remap = false)
    float getPageLeft();

    @Accessor(value = "pageTop", remap = false)
    float getPageTop();
}
