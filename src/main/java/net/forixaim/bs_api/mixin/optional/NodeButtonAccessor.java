package net.forixaim.bs_api.mixin.optional;

import com.yesman.epicskills.client.gui.screen.SkillTreeScreen;
import com.yesman.epicskills.world.capability.SkillTreeProgression;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SkillTreeScreen.TreePage.NodeButton.class)
public interface NodeButtonAccessor {

    @Accessor(value = "treeNode", remap = false)
    SkillTreeProgression.TopDownTreeNode getTreeNode();
}
