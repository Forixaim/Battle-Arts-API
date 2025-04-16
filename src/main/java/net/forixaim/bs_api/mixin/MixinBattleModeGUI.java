package net.forixaim.bs_api.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.forixaim.bs_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.bs_api.battle_arts_skills.battle_style.BattleStyle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.utils.math.Vec2f;
import yesman.epicfight.api.utils.math.Vec2i;
import yesman.epicfight.client.gui.BattleModeGui;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.config.ClientConfig;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;

@Mixin(value = BattleModeGui.class, remap = false)
public abstract class MixinBattleModeGUI
{
    @Shadow private int sliding;

    @Shadow @Final private static Vec2f[] CLOCK_POS;


    @Shadow public abstract Font getFont();

    @Inject(method = "drawWeaponInnateIcon", at = @At("HEAD"), remap = false, cancellable = true)
    private void drawWeaponInnate(LocalPlayerPatch playerpatch, SkillContainer container, GuiGraphics guiGraphics, float partialTicks, CallbackInfo ci)
    {
        if (playerpatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getSkill() instanceof BattleStyle battleStyle)
        {
            battleArtsAPI$drawWeaponInnateIconSP(playerpatch, container, guiGraphics, partialTicks, battleStyle);
            ci.cancel();
        }
    }

    @Unique
    private void battleArtsAPI$drawWeaponInnateIconSP(LocalPlayerPatch playerpatch, SkillContainer container, GuiGraphics guiGraphics, float partialTicks, BattleStyle battleStyle) {
        PoseStack poseStack = guiGraphics.pose();
        Window sr = Minecraft.getInstance().getWindow();
        int width = sr.getGuiScaledWidth();
        int height = sr.getGuiScaledHeight();
        Vec2i pos = ClientConfig.getWeaponInnatePosition(width, height);

        poseStack.pushPose();
        poseStack.translate(0, this.sliding, 0);

        boolean creative = playerpatch.getOriginal().isCreative();
        boolean fullstack = creative || container.isFull();
        boolean canUse = !container.isDisabled() && container.getSkill().checkExecuteCondition(container);
        float cooldownRatio = (fullstack || container.isActivated()) ? 1.0F : container.getResource(partialTicks);
        int vertexNum = 0;
        float iconSize = 32.0F;
        float bottom = pos.y + iconSize;
        float right = pos.x + iconSize;
        float middle = pos.x + iconSize * 0.5F;
        float lastVertexX = 0;
        float lastVertexY = 0;
        float lastTexX = 0;
        float lastTexY = 0;

        if (cooldownRatio < 0.125F) {
            vertexNum = 6;
            lastTexX = cooldownRatio / 0.25F;
            lastTexY = 0.0F;
            lastVertexX = middle + iconSize * lastTexX;
            lastVertexY = pos.y;
            lastTexX += 0.5F;
        } else if (cooldownRatio < 0.375F) {
            vertexNum = 5;
            lastTexX = 1.0F;
            lastTexY = (cooldownRatio - 0.125F) / 0.25F;
            lastVertexX = right;
            lastVertexY = pos.y + iconSize * lastTexY;
        } else if (cooldownRatio < 0.625F) {
            vertexNum = 4;
            lastTexX = (cooldownRatio - 0.375F) / 0.25F;
            lastTexY = 1.0F;
            lastVertexX = right - iconSize * lastTexX;
            lastVertexY = bottom;
            lastTexX = 1.0F - lastTexX;
        } else if (cooldownRatio < 0.875F) {
            vertexNum = 3;
            lastTexX = 0.0F;
            lastTexY = (cooldownRatio - 0.625F) / 0.25F;
            lastVertexX = pos.x;
            lastVertexY = bottom - iconSize * lastTexY;
            lastTexY = 1.0F - lastTexY;
        } else {
            vertexNum = 2;
            lastTexX = (cooldownRatio - 0.875F) / 0.25F;
            lastTexY = 0.0F;
            lastVertexX = pos.x + iconSize * lastTexX;
            lastVertexY = pos.y;
        }

        RenderSystem.enableBlend();
        RenderSystem.setShaderTexture(0, container.getSkill().getSkillTexture());
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        if (canUse) {
            if (container.getStack() > 0) {
                RenderSystem.setShaderColor(battleStyle.getInnateSkillColor()[0], battleStyle.getInnateSkillColor()[1], battleStyle.getInnateSkillColor()[2], 0.8f);
            } else {
                RenderSystem.setShaderColor(battleStyle.getInnateSkillColor()[0], battleStyle.getInnateSkillColor()[1], battleStyle.getInnateSkillColor()[2], 0.6F);
            }
        } else {
            RenderSystem.setShaderColor(battleStyle.getInnateInactiveColor()[0], battleStyle.getInnateInactiveColor()[1], battleStyle.getInnateInactiveColor()[2], 0.6F);
        }

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_TEX);

        for (int j = 0; j < vertexNum; j++) {
            bufferbuilder.vertex(poseStack.last().pose(), pos.x + iconSize * CLOCK_POS[j].x, pos.y + iconSize * CLOCK_POS[j].y, 0.0F).uv(CLOCK_POS[j].x, CLOCK_POS[j].y).endVertex();
        }

        bufferbuilder.vertex(poseStack.last().pose(), lastVertexX, lastVertexY, 0.0F).uv(lastTexX, lastTexY).endVertex();
        tessellator.end();

        if (canUse) {
            RenderSystem.setShaderColor(battleStyle.getInnateSkillColor()[0], battleStyle.getInnateSkillColor()[1], battleStyle.getInnateSkillColor()[2], 1.0F);
        } else {
            RenderSystem.setShaderColor(battleStyle.getInnateInactiveColor()[0], battleStyle.getInnateInactiveColor()[1], battleStyle.getInnateInactiveColor()[2], 1.0F);
        }

        GL11.glCullFace(GL11.GL_FRONT);

        bufferbuilder.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_TEX);

        for (int j = 0; j < 2; j++) {
            bufferbuilder.vertex(poseStack.last().pose(), pos.x + iconSize * CLOCK_POS[j].x, pos.y + iconSize * CLOCK_POS[j].y, 0.0F).uv(CLOCK_POS[j].x, CLOCK_POS[j].y).endVertex();
        }

        for (int j = CLOCK_POS.length - 1; j >= vertexNum; j--) {
            bufferbuilder.vertex(poseStack.last().pose(), pos.x + iconSize * CLOCK_POS[j].x, pos.y + iconSize * CLOCK_POS[j].y, 0.0F).uv(CLOCK_POS[j].x, CLOCK_POS[j].y).endVertex();
        }

        bufferbuilder.vertex(poseStack.last().pose(), lastVertexX, lastVertexY, 0.0F).uv(lastTexX, lastTexY).endVertex();
        tessellator.end();

        GL11.glCullFace(GL11.GL_BACK);

        RenderSystem.setShaderColor(battleStyle.getInnateInactiveColor()[0], battleStyle.getInnateInactiveColor()[1], battleStyle.getInnateInactiveColor()[2], 1.0F);

        if (container.isActivated() && (container.getSkill().getActivateType() == Skill.ActivateType.DURATION || container.getSkill().getActivateType() == Skill.ActivateType.DURATION_INFINITE)) {
            String s = String.format("%.0f", container.getRemainDuration() / 20.0F);
            int stringWidth = (this.getFont().width(s) - 6) / 3;
            guiGraphics.drawString(this.getFont(), s, pos.x + 13 - stringWidth, pos.y + 13, 16777215, true);
        } else if (!fullstack) {
            String s = String.valueOf((int)(cooldownRatio * 100.0F));
            int stringWidth = (this.getFont().width(s) - 6) / 3;
            guiGraphics.drawString(this.getFont(), s, pos.x + 13 - stringWidth, pos.y + 13, 16777215, true);
        }

        if (container.getSkill().getMaxStack() > 1) {
            String s = String.valueOf(container.getStack());
            int stringWidth = (this.getFont().width(s) - 6) / 3;
            guiGraphics.drawString(getFont(), s, pos.x + 25 - stringWidth, pos.y + 22, 16777215, true);
        }

        poseStack.popPose();
    }
}
