package net.forixaim.battle_arts_api.battle_arts_skills.battle_style;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.forixaim.battle_arts_api.BattleArtsAPI;
import net.forixaim.battle_arts_api.Config;
import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillCategories;
import net.forixaim.battle_arts_api.battle_arts_skills.CoreAPIDataKeys;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.event.EntityEventListener;
import yesman.epicfight.client.gui.BattleModeGui;
import yesman.epicfight.skill.*;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.WeaponCapability;
import yesman.epicfight.world.capabilities.item.WeaponCategory;

import java.util.*;
import java.util.stream.IntStream;

/**
 * This class extends the skill class and is mainly there as a framework for the Battle Styles which can modify certain things.
 */
@SuppressWarnings("unchecked")
public abstract class BattleStyle extends Skill
{
	private static final UUID UNIVERSAL_BATTLE_STYLE_UUID = UUID.fromString("705bfb84-a7c1-4726-bc7b-36cbabb84843");

	protected float jumpBoostPower = 0.0F;

    private static final ResourceLocation CONTAINER_TEX = BattleArtsAPI.identifier("textures/gui/meter/container.png");
    private static final ResourceLocation OVERLAY_TEX   = BattleArtsAPI.identifier("textures/gui/meter/overlay.png");

	private static final List<ResourceLocation> BARS = IntStream.rangeClosed(1, 10)
			.mapToObj(i -> BattleArtsAPI.identifier("textures/gui/meter/super_meter_" + i + ".png")).toList();

	private static final List<ResourceLocation> METER_ICONS = IntStream.rangeClosed(0, 10)
			.mapToObj(i -> BattleArtsAPI.identifier("textures/gui/meter/super_icon_" + i + ".png")).toList();

    protected int maxMeter = 0;

	//From 0.0 to 1.0
	protected float criticalHitChance = 0.5F;
	protected float criticalHitDamage = 0.5F;

	protected BattleStyleCategory category;

	protected List<AnimationManager.AnimationAccessor<? extends AttackAnimation>> unarmedAttackAnimations;
	protected Map<LivingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>> unarmedLivingMotions;
	protected Map<LivingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>> unarmedBattleMotions;
	protected Map<GuardSkill, Map<GuardSkill.BlockType, List<AnimationManager.AnimationAccessor<? extends StaticAnimation>>>> guardMaps;
	protected Skill unarmedInnateSkill;
	protected Skill unarmedPassiveSkill;
	protected final List<Skill> dependentSkills;


	protected Map<WeaponCategory, AnimationManager.AnimationAccessor<? extends StaticAnimation>> weaponDrawAnimations;

	/**
	 * super constructor must be called when creating a new battle style.
	 * @param builder the builder.
	 */
	public BattleStyle(SkillBuilder<?> builder)
	{
		super (builder);
		this.unarmedAttackAnimations = Lists.newArrayList();
		this.unarmedLivingMotions = Maps.newHashMap();
		this.unarmedBattleMotions = Maps.newHashMap();
		this.weaponDrawAnimations = Maps.newHashMap();
		this.dependentSkills = Lists.newArrayList();
		this.guardMaps = Maps.newHashMap();
		this.unarmedInnateSkill = null;
		this.unarmedPassiveSkill = null;
	}

	public Skill getUnarmedInnateSkill()
	{
		return unarmedInnateSkill;
	}

	public Map<GuardSkill, Map<GuardSkill.BlockType, List<AnimationManager.AnimationAccessor<? extends StaticAnimation>>>> getGuardMaps() {
		return guardMaps;
	}


	public boolean modifiesUnarmedLMs()
	{
		return this.unarmedLivingMotions != null && !this.unarmedLivingMotions.isEmpty();
	}


	public Map<LivingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>> getUnarmedLivingMotions()
	{
		return unarmedLivingMotions;
	}

    @Override
    public void loadDatapackParameters(CompoundTag parameters) {
        super.loadDatapackParameters(parameters);
        if (parameters.contains("max_meter"))
        {
            maxMeter = parameters.getInt("max_meter");
        }
    }


	public float getJumpBoostPower()
	{
		return jumpBoostPower;
	}

	public float getCriticalHitDamage()
	{
		return criticalHitDamage;
	}

	@Override
	public void onInitiate(SkillContainer container, EntityEventListener eventListener)
	{

		if (container.getExecutor() instanceof ServerPlayerPatch spp)
		{
			if (!(spp.getHoldingItemCapability(InteractionHand.MAIN_HAND) instanceof WeaponCapability))
				spp.modifyLivingMotionByCurrentItem(false);
		}
	}

    @Override
    public boolean shouldDraw(SkillContainer container)
    {

        return this.maxMeter > 0;
    }


	@Override
	public void onRemoved(SkillContainer container)
	{
		if (container.getExecutor() instanceof ServerPlayerPatch spp && !(spp.getHoldingItemCapability(InteractionHand.MAIN_HAND) instanceof WeaponCapability))
		{
			spp.modifyLivingMotionByCurrentItem(false);
		}

	}


    @Override
    public void updateContainer(SkillContainer container) {
        super.updateContainer(container);
        if (BattleArtsAPI.debugMode && container.getDataManager().getDataValue(CoreAPIDataKeys.METER_FILL) < (maxMeter * 100) && !container.getExecutor().isLogicalClient()) {
            container.getDataManager().setDataSyncF(CoreAPIDataKeys.METER_FILL, data -> data + 1);
        }
    }

    public int getMaxMeter() {
        return maxMeter;
    }

    @OnlyIn(Dist.CLIENT)
	@Override
	public void drawOnGui(BattleModeGui gui, SkillContainer container, GuiGraphics guiGraphics, float x, float y, float pt) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        float meterLevel = container.getDataManager().getDataValue(CoreAPIDataKeys.METER_FILL);
        poseStack.translate(Config.superMeterPositionX, Config.superMeterPositionY, 0);
        poseStack.scale((float) Config.superMeterScaleX, (float) Config.superMeterScaleY, 1f);
        guiGraphics.blit(CONTAINER_TEX, (int)x, (int)y, 0, 0, 128, 8, 128, 8);
        for (int i = Math.max(0, (int) meterLevel / 100 - 1); i < (int) meterLevel / 100 && i < 10; i++) {
            ResourceLocation tex = BARS.get(Math.min(i, BARS.size() - 1));
            guiGraphics.blit(tex, (int) x, (int) y, 0, 0, 128, 8, 128, 8);
        }
        if (meterLevel / 100 < BARS.size() && meterLevel % 100 > 0) {
            float percent = (int)meterLevel % 100 / (float)100;
            int filledWidth = (int)(128 * percent);
            ResourceLocation tex = BARS.get((int) Math.min(meterLevel / 100, BARS.size() - 1));
            guiGraphics.blit(tex, (int) (x), (int) y, 0, 0, filledWidth, 8,  filledWidth, 8);
        }
        guiGraphics.blit(OVERLAY_TEX, (int) x, (int) y, 0, 0, 128, 8, 128, 8);
        int iconIndex = (int) ((meterLevel / 100) % METER_ICONS.size());
        guiGraphics.blit(METER_ICONS.get(iconIndex), (int) x - 32, (int) y - 12, 0, 0, 32, 32, 32, 32);
        poseStack.popPose();
	}
}
