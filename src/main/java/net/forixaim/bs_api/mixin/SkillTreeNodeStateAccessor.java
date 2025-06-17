package net.forixaim.bs_api.mixin;

import com.yesman.epicskills.client.gui.screen.SkillTreeScreen;
import com.yesman.epicskills.world.capability.SkillTreeProgression;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(SkillTreeScreen.TreePage.NodeButton.ButtonStateTexture.class)
public interface SkillTreeNodeStateAccessor {
    @Accessor(value = "STATE_MAPPING", remap = false)
    static Map<SkillTreeProgression.NodeState, SkillTreeScreen.TreePage.NodeButton.ButtonStateTexture> getStateMapping() {
        throw new UnsupportedOperationException("fuck you");
    }

    @Accessor(value = "r", remap = false)
    int getR();

    @Accessor(value = "g", remap = false)
    int getG();

    @Accessor(value = "b", remap = false)
    int getB();
}
