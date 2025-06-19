package net.forixaim.bs_api.mixin.optional;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.datafixers.util.Pair;
import com.yesman.epicskills.client.gui.screen.SkillTreeScreen;
import com.yesman.epicskills.registry.SkillTree;
import com.yesman.epicskills.world.capability.SkillTreeProgression;
import net.forixaim.bs_api.battle_arts_skills.BattleArtsSkillCategories;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.utils.ParseUtil;
import yesman.epicfight.api.utils.math.Vec2i;
import yesman.epicfight.skill.Skill;

import java.lang.reflect.Field;
import java.util.List;

@Mixin(SkillTreeScreen.TreePage.NodeButton.class)
public abstract class MixinSkillTreeNodeButton
{
    @Shadow(remap = false) @Final
    SkillTreeScreen.TreePage this$1;

    @Unique private SkillTreeScreen.TreePage.NodeButton bart$self = (SkillTreeScreen.TreePage.NodeButton) (Object) this;

    @Shadow(remap = false) @Final private boolean importedNode;

    @Shadow(remap = false) public abstract Skill getSkill();

    @Shadow(remap = false) @Final private SkillTreeProgression.TopDownTreeNode treeNode;

    @Shadow(remap = false) @Final private SkillTreeScreen.TreePage.NodeButton.CategorySlotTexture categoryTexture;

    @Shadow(remap = false) @Final private List<Pair<SkillTreeScreen.TreePage.NodeButton, List<Vec2i>>> parents;

    @Inject(method = "renderWidget", at = @At("HEAD"), remap = false, cancellable = true)
    public void injectRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci)
    {
        try
        {
            Field outerField = this$1.getClass().getDeclaredField("this$0");
            outerField.setAccessible(true);
            SkillTreeScreen outer = (SkillTreeScreen) outerField.get(this$1);
            mouseX = (int)((float)mouseX - ((TreePageAccessor) this$1).getPageLeft());
            mouseY = (int)((float)mouseY - ((TreePageAccessor) this$1).getPageTop());
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0.0F, 0.0F, 100.0F);
            SkillTreeScreen.TreePage.NodeButton.ButtonStateTexture buttonTexture = SkillTreeNodeStateAccessor.getStateMapping().get(this.treeNode.nodeState());
            if (buttonTexture == SkillTreeScreen.TreePage.NodeButton.ButtonStateTexture.ACQUIRED && ((SkillTreeScreenAccessor) outer).getPlayerSkills().isEquipping(this.getSkill())) {
                buttonTexture = SkillTreeScreen.TreePage.NodeButton.ButtonStateTexture.EQUIPPED;
            }

            int r, g, b;

            try
            {
                Field redField = buttonTexture.getClass().getDeclaredField("r");
                Field greenField = buttonTexture.getClass().getDeclaredField("g");
                Field blueField = buttonTexture.getClass().getDeclaredField("b");
                redField.setAccessible(true);
                greenField.setAccessible(true);
                blueField.setAccessible(true);
                r = redField.getInt(buttonTexture);
                g = greenField.getInt(buttonTexture);
                b = blueField.getInt(buttonTexture);
            }
            catch (Throwable e)
            {
                r = 255;
                g = 255;
                b = 255;
            }

            ResourceLocation nodeTexture = ResourceLocation.fromNamespaceAndPath("epicskills", String.format("textures/gui/widget/node/%s/%s.png", ParseUtil.toLowerCase(this.treeNode.nodeInfo().skill().getCategory().toString()), ParseUtil.toLowerCase(buttonTexture.name())));
            if (this.importedNode) {
                RenderSystem.enableBlend();
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.4F);
            }

            int widthHalf = bart$self.getWidth() / 2;
            int heightHalf = bart$self.getHeight() / 2;
            if (getSkill().getCategory() == BattleArtsSkillCategories.BATTLE_STYLE)
            {
                guiGraphics.blit(nodeTexture, bart$self.getX() - widthHalf - 6, bart$self.getY() - heightHalf - 6, 48, 48, 0.0F, 0.0F, 48, 48, 48, 48);
            }
            else
            {
                int offsetX, offsetY, width, height ;
                try
                {
                    Field offsetXField = categoryTexture.getClass().getDeclaredField("offsetX");
                    Field offsetYField = categoryTexture.getClass().getDeclaredField("offsetY");
                    Field widthField = categoryTexture.getClass().getDeclaredField("texWidth");
                    Field heightField = categoryTexture.getClass().getDeclaredField("texHeight");
                    offsetXField.setAccessible(true);
                    offsetYField.setAccessible(true);
                    widthField.setAccessible(true);
                    heightField.setAccessible(true);
                    offsetX = offsetXField.getInt(categoryTexture);
                    offsetY = offsetYField.getInt(categoryTexture);
                    width = widthField.getInt(categoryTexture);
                    height = heightField.getInt(categoryTexture);

                }
                catch (Throwable e)
                {
                    offsetX = 0;
                    offsetY = 0;
                    width = 16;
                    height = 16;
                }
                guiGraphics.blit(nodeTexture, bart$self.getX() - widthHalf - offsetX, bart$self.getY() - heightHalf - offsetY, width, height, 0.0F, 0.0F, width, height, width, height);
            }
            guiGraphics.innerBlit(this.treeNode.nodeInfo().skill().getSkillTexture(), bart$self.getX() - widthHalf, bart$self.getX() + widthHalf, bart$self.getY() - heightHalf, bart$self.getY() + heightHalf, 0, 0.0F, 1.0F, 0.0F, 1.0F, (float)r / 255.0F, (float)g / 255.0F, (float)b / 255.0F, 1.0F);
            if (buttonTexture == SkillTreeScreen.TreePage.NodeButton.ButtonStateTexture.LOCKED && !this.treeNode.nodeInfo().noUnlockConditions()) {
                guiGraphics.innerBlit(ResourceLocation.fromNamespaceAndPath("epicskills", "textures/gui/widget/locker.png"), bart$self.getX() - widthHalf, bart$self.getX() + widthHalf, bart$self.getY() - heightHalf, bart$self.getY() + heightHalf, 0, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            }

            if (this.importedNode) {
                RenderSystem.disableBlend();
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            }

            if (!((SkillTreeScreenAccessor)outer).getBackgroundMode() && bart$self.getX() - widthHalf < mouseX && mouseX < bart$self.getX() + widthHalf && bart$self.getY() - heightHalf < mouseY && mouseY < bart$self.getY() + heightHalf) {
                if (this.importedNode) {
                    outer.setTooltipForNextRenderPass(List.of(this.treeNode.nodeInfo().skill().getDisplayName().getVisualOrderText(), Component.translatable("gui.epicskills.skill_tree.imported_node", new Object[]{Component.translatable(SkillTree.toDescriptionId(((SkillTreeProgression.ImportedNode)this.treeNode).getOriginalTree().key())).getString()}).getVisualOrderText()));
                } else {
                    outer.setTooltipForNextRenderPass(List.of(this.treeNode.nodeInfo().skill().getDisplayName().getVisualOrderText()));
                }
            }

            if ((buttonTexture == SkillTreeScreen.TreePage.NodeButton.ButtonStateTexture.LOCKED || buttonTexture == SkillTreeScreen.TreePage.NodeButton.ButtonStateTexture.UNLOCKABLE) && !this.importedNode) {
                int markerX = bart$self.getX() + widthHalf - 7;
                int markerY = bart$self.getY() + heightHalf - 8;
                guiGraphics.fill(markerX, markerY, markerX + 11, markerY + 12, -9934744);
                guiGraphics.fill(markerX + 1, markerY + 1, markerX + 10, markerY + 11, -16777216);
                guiGraphics.drawString(outer.getMinecraft().font, String.valueOf(this.treeNode.nodeInfo().requiredAbilityPoints()), markerX + 3, markerY + 2, ((SkillTreeScreenAccessor)outer).getPlayerAbilityPoints().getAbilityPoints() >= this.treeNode.nodeInfo().requiredAbilityPoints() ? -1 : -65536);
            }

            guiGraphics.pose().popPose();

            for(Pair<SkillTreeScreen.TreePage.NodeButton, List<Vec2i>> parentTree : this.parents) {
                SkillTreeScreen.TreePage.NodeButton parentButton = parentTree.getFirst();
                List<Vec2i> controlPoints = Lists.newArrayList();
                controlPoints.add(new Vec2i(parentButton.getX(), parentButton.getY()));
                if (parentTree.getSecond() != null && !(parentTree.getSecond()).isEmpty()) {
                    controlPoints.addAll(parentTree.getSecond());
                } else {
                    controlPoints.add(new Vec2i(bart$self.getX(), parentButton.getY()));
                }

                controlPoints.add(new Vec2i(bart$self.getX(), bart$self.getY()));
                boolean unlocked = ((NodeButtonAccessor) parentTree.getFirst()).getTreeNode().nodeState() == SkillTreeProgression.NodeState.UNLOCKED;
                if (unlocked) {
                    guiGraphics.pose().pushPose();
                    guiGraphics.pose().translate(0.0F, 0.0F, 1.0F);
                }

                for (int i = 0; i < controlPoints.size() - 1; ++i) {
                    Vec2i p1 = controlPoints.get(i);
                    Vec2i p2 = controlPoints.get(i + 1);
                    Tesselator tesselator = Tesselator.getInstance();
                    BufferBuilder bufferBuilder = tesselator.getBuilder();
                    float xDiff = (float) (p2.x - p1.x);
                    float yDiff = (float) (p2.y - p1.y);
                    float length = Mth.sqrt(xDiff * xDiff + yDiff * yDiff);
                    xDiff /= length;
                    yDiff /= length;
                    bufferBuilder.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR_NORMAL);
                    bufferBuilder.vertex(guiGraphics.pose().last().pose(), (float) p1.x + 0.5F, (float) p1.y + 0.5F, 0.0F).color(unlocked ? -2698555 : -16777216).normal(xDiff, yDiff, 0.0F).endVertex();
                    bufferBuilder.vertex(guiGraphics.pose().last().pose(), (float) p2.x + 0.5F, (float) p2.y + 0.5F, 0.0F).color(unlocked ? -2698555 : -16777216).normal(xDiff, yDiff, 0.0F).endVertex();
                    RenderSystem.lineWidth(2.0F * (float) Minecraft.getInstance().getWindow().getGuiScale());
                    RenderSystem.setShader(GameRenderer::getRendertypeLinesShader);
                    BufferUploader.drawWithShader(bufferBuilder.end());
                }

                if (unlocked) {
                    guiGraphics.pose().popPose();
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        ci.cancel();
    }
}
