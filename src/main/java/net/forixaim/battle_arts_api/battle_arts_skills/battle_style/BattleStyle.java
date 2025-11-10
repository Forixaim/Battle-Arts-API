package net.forixaim.battle_arts_api.battle_arts_skills.battle_style;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import net.forixaim.battle_arts_api.BattleArtsAPI;
import net.forixaim.battle_arts_api.Config;
import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillCategories;
import net.forixaim.battle_arts_api.battle_arts_skills.CoreAPIDataKeys;
import net.forixaim.battle_arts_api.battle_arts_skills.passive.BattleStyleDependentPassive;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.registries.ForgeRegistries;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.utils.ParseUtil;
import yesman.epicfight.client.gui.BattleModeGui;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.network.server.SPChangeSkill;
import yesman.epicfight.skill.*;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.WeaponCapability;
import yesman.epicfight.world.capabilities.item.WeaponCategory;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.*;

/**
 * This class extends the skill class and is mainly there as a framework for the Battle Styles which can modify certain things.
 */
@SuppressWarnings("unchecked")
public abstract class BattleStyle extends Skill
{
	private static final UUID UNIVERSAL_BATTLE_STYLE_UUID = UUID.fromString("705bfb84-a7c1-4726-bc7b-36cbabb84843");

	public static Builder<BattleStyle> CreateBattleStyle()
	{
		return (new Builder<>().setCategory(BattleArtsSkillCategories.BATTLE_STYLE).setResource(Resource.NONE));
	}

	/**
	 * This array sets the innate skill color of a battle style.
	 * It must be of size 3 with a float value between 0 and 1
	 * [0]: Red
	 * [1]: Green
	 * [2]: Blue
	 */
	protected float[] innateSkillColor;

	/**
	 * This array sets the inactive innate skill color of a battle style.
	 * It must be of size 3 with a float value between 0 and 1
	 * [0]: Red
	 * [1]: Green
	 * [2]: Blue
	 */
	protected float[] innateInactiveColor;

	protected int proficiencyXpPerKill = 0;
	protected float jumpBoostPower = 0.0F;

    private static final ResourceLocation CONTAINER_TEX = ResourceLocation.fromNamespaceAndPath(BattleArtsAPI.MOD_ID, "textures/gui/meter/container.png");
    private static final ResourceLocation OVERLAY_TEX   = ResourceLocation.fromNamespaceAndPath(BattleArtsAPI.MOD_ID, "textures/gui/meter/overlay.png");

    private static final List<ResourceLocation> BARS = Lists.newArrayList(
            ResourceLocation.fromNamespaceAndPath(BattleArtsAPI.MOD_ID, "textures/gui/meter/super_meter_1.png"),
            ResourceLocation.fromNamespaceAndPath(BattleArtsAPI.MOD_ID, "textures/gui/meter/super_meter_2.png"),
            ResourceLocation.fromNamespaceAndPath(BattleArtsAPI.MOD_ID, "textures/gui/meter/super_meter_3.png"),
            ResourceLocation.fromNamespaceAndPath(BattleArtsAPI.MOD_ID, "textures/gui/meter/super_meter_4.png"),
            ResourceLocation.fromNamespaceAndPath(BattleArtsAPI.MOD_ID, "textures/gui/meter/super_meter_5.png"),
            ResourceLocation.fromNamespaceAndPath(BattleArtsAPI.MOD_ID, "textures/gui/meter/super_meter_6.png"),
            ResourceLocation.fromNamespaceAndPath(BattleArtsAPI.MOD_ID, "textures/gui/meter/super_meter_7.png"),
            ResourceLocation.fromNamespaceAndPath(BattleArtsAPI.MOD_ID, "textures/gui/meter/super_meter_8.png"),
            ResourceLocation.fromNamespaceAndPath(BattleArtsAPI.MOD_ID, "textures/gui/meter/super_meter_9.png"),
            ResourceLocation.fromNamespaceAndPath(BattleArtsAPI.MOD_ID, "textures/gui/meter/super_meter_10.png")
    );

    private static final List<ResourceLocation> METER_ICONS = Lists.newArrayList(
            ResourceLocation.fromNamespaceAndPath(BattleArtsAPI.MOD_ID, "textures/gui/meter/super_icon_0.png"),
            ResourceLocation.fromNamespaceAndPath(BattleArtsAPI.MOD_ID, "textures/gui/meter/super_icon_1.png"),
            ResourceLocation.fromNamespaceAndPath(BattleArtsAPI.MOD_ID, "textures/gui/meter/super_icon_2.png"),
            ResourceLocation.fromNamespaceAndPath(BattleArtsAPI.MOD_ID, "textures/gui/meter/super_icon_3.png"),
            ResourceLocation.fromNamespaceAndPath(BattleArtsAPI.MOD_ID, "textures/gui/meter/super_icon_4.png"),
            ResourceLocation.fromNamespaceAndPath(BattleArtsAPI.MOD_ID, "textures/gui/meter/super_icon_5.png"),
            ResourceLocation.fromNamespaceAndPath(BattleArtsAPI.MOD_ID, "textures/gui/meter/super_icon_6.png"),
            ResourceLocation.fromNamespaceAndPath(BattleArtsAPI.MOD_ID, "textures/gui/meter/super_icon_7.png"),
            ResourceLocation.fromNamespaceAndPath(BattleArtsAPI.MOD_ID, "textures/gui/meter/super_icon_8.png"),
            ResourceLocation.fromNamespaceAndPath(BattleArtsAPI.MOD_ID, "textures/gui/meter/super_icon_9.png"),
            ResourceLocation.fromNamespaceAndPath(BattleArtsAPI.MOD_ID, "textures/gui/meter/super_icon_10.png")
    );

    protected int maxMeter = 0;

	//From 0.0 to 1.0
	protected float criticalHitChance = 0.5F;
	protected float criticalHitDamage = 0.5F;

	protected List<ResourceKey<DamageType>> immuneDamages;
	protected List<TagKey<DamageType>> immuneModdedDamages;
	protected BattleStyleCategory category;

	protected List<AnimationManager.AnimationAccessor<? extends AttackAnimation>> unarmedAttackAnimations;
	protected Map<LivingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>> unarmedLivingMotions;
	protected Map<LivingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>> unarmedBattleMotions;
	protected Map<GuardSkill, Map<GuardSkill.BlockType, AnimationManager.AnimationAccessor<? extends StaticAnimation>>> guardMaps;
	protected Skill unarmedInnateSkill;
	protected Skill unarmedPassiveSkill;
	protected final List<Skill> dependentSkills;


	private final Map<Attribute, AttributeModifier> BattleStyleStatModifier;
	protected Map<WeaponCategory, AnimationManager.AnimationAccessor<? extends StaticAnimation>> weaponDrawAnimations;

	/**
	 * super constructor must be called when creating a new battle style.
	 * @param builder the builder.
	 */
	public BattleStyle(Builder<?> builder)
	{
		super (builder);
		this.innateSkillColor = new float[]{0.0F, 0.64F, 0.72F};
		this.innateInactiveColor = new float[]{0.5f, 0.5f, 0.5f};
		this.unarmedAttackAnimations = Lists.newArrayList();
		this.unarmedLivingMotions = Maps.newHashMap();
		this.unarmedBattleMotions = Maps.newHashMap();
		this.BattleStyleStatModifier = Maps.newHashMap();
		this.immuneDamages = Lists.newArrayList();
		this.immuneModdedDamages = Lists.newArrayList();
		this.weaponDrawAnimations = Maps.newHashMap();
		this.dependentSkills = Lists.newArrayList();
		this.guardMaps = Maps.newHashMap();
		this.unarmedInnateSkill = null;
		this.unarmedPassiveSkill = null;
		this.category = builder.battleStyleCategory;
	}

	public float[] getInnateInactiveColor()
	{
		return innateInactiveColor;
	}

	public Skill getUnarmedInnateSkill()
	{
		return unarmedInnateSkill;
	}

	public Map<GuardSkill, Map<GuardSkill.BlockType, AnimationManager.AnimationAccessor<? extends StaticAnimation>>> getGuardMaps() {
		return guardMaps;
	}

	public boolean unarmedMoveset()
	{
		return false;
	}

	public Skill getUnarmedPassiveSkill()
	{
		return unarmedPassiveSkill;
	}

	public boolean modifiesUnarmedAttacks()
	{
		return this.unarmedAttackAnimations != null && !this.unarmedAttackAnimations.isEmpty();
	}

	public boolean modifiesUnarmedLMs()
	{
		return this.unarmedLivingMotions != null && !this.unarmedLivingMotions.isEmpty();
	}

	public boolean modifiesUnarmedBMs()
	{
		return this.unarmedBattleMotions != null && !this.unarmedBattleMotions.isEmpty();
	}

	public Map<LivingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>> getUnarmedBattleMotions()
	{
		return unarmedBattleMotions;
	}

	public List<AnimationManager.AnimationAccessor<? extends AttackAnimation>> getUnarmedAttackAnimations()
	{
		return unarmedAttackAnimations;
	}

	public Map<LivingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>> getUnarmedLivingMotions()
	{
		return unarmedLivingMotions;
	}

	public int getProficiencyBonus()
	{
		return proficiencyXpPerKill;
	}

	public static Builder<BattleStyle> createBattleStyleBuilder()
	{
		return new Builder<>().setCategory(BattleArtsSkillCategories.BATTLE_STYLE).setResource(Resource.NONE);
	}

	public List<ResourceKey<DamageType>> getImmuneDamages()
	{
		return immuneDamages;
	}
	public List<TagKey<DamageType>> getImmuneModdedDamages()
	{
		return immuneModdedDamages;
	}

	@Override
	public void setParams(CompoundTag parameters) {
		super.setParams(parameters);

        if (parameters.contains("max_meter"))
        {
            maxMeter = parameters.getInt("max_meter");
        }

		this.BattleStyleStatModifier.clear();
		if (parameters.contains("attribute_modifiers")) {
			ListTag attributeList = parameters.getList("attribute_modifiers", 10);

			for (Tag tag : attributeList) {
				CompoundTag comp = (CompoundTag)tag;
				String attribute = comp.getString("attribute");
				Attribute attr = ForgeRegistries.ATTRIBUTES.getValue(ResourceLocation.parse(attribute));
				AttributeModifier modifier = ParseUtil.toAttributeModifier(comp);

				this.BattleStyleStatModifier.put(attr, modifier);
			}
		}
		jumpBoostPower = parameters.getFloat("jump_boost_power");
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
	public void onInitiate(SkillContainer container)
	{
		if (container.getExecutor() instanceof ServerPlayerPatch spp)
		{
			if (!(spp.getHoldingItemCapability(InteractionHand.MAIN_HAND) instanceof WeaponCapability))
				spp.modifyLivingMotionByCurrentItem(false);
		}

        BattleStyleStatModifier.forEach((attribute, attributeModifier) -> {
                    if ((container.getExecutor().getOriginal().getAttribute(attribute) != null && Objects.requireNonNull(container.getExecutor().getOriginal().getAttribute(attribute)).hasModifier(attributeModifier))) {
                        Objects.requireNonNull(container.getExecutor().getOriginal().getAttribute(attribute)).addTransientModifier(attributeModifier);
                    }
                }
        );
		container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.DEAL_DAMAGE_EVENT_HURT, UNIVERSAL_BATTLE_STYLE_UUID, event ->
		{
			float random = container.getExecutor().getOriginal().getRandom().nextFloat();
			CriticalHitEvent crit = ForgeHooks.getCriticalHit(event.getPlayerPatch().getOriginal(), event.getTarget(), false, random <= criticalHitChance ? 1 + getCriticalHitDamage() : 1.0f);
			if (crit != null)
			{
				event.getPlayerPatch().playSound(SoundEvents.PLAYER_ATTACK_CRIT, 1.0F, 1.0F);
			}

		});
	}

    @Override
    public boolean shouldDraw(SkillContainer container)
    {

        return this.maxMeter > 0;
    }


    private void removeBattleStyleDependentSkills(ServerPlayerPatch playerPatch)
	{
		EpicFightNetworkManager.PayloadBundleBuilder toLocal = EpicFightNetworkManager.PayloadBundleBuilder.create();

		for (SkillContainer skillContainer : playerPatch.getSkillCapability().skillContainers)
		{
			if (skillContainer.getSkill() instanceof BattleStyleDependentPassive bsd)
			{
				if (bsd.isApplicable(this))
				{
					skillContainer.setSkill(null);
					toLocal.and(new SPChangeSkill(SkillSlots.WEAPON_INNATE, playerPatch.getOriginal().getId(), this));
				}
			}
		}
	}

	@Override
	public void onRemoved(SkillContainer container)
	{
		if (container.getExecutor() instanceof ServerPlayerPatch spp && !(spp.getHoldingItemCapability(InteractionHand.MAIN_HAND) instanceof WeaponCapability))
		{
			spp.modifyLivingMotionByCurrentItem(false);
			removeBattleStyleDependentSkills(spp);
		}

        BattleStyleStatModifier.forEach((attribute, attributeModifier) -> {
                    if ((container.getExecutor().getOriginal().getAttribute(attribute) != null && Objects.requireNonNull(container.getExecutor().getOriginal().getAttribute(attribute)).hasModifier(attributeModifier))) {
                        Objects.requireNonNull(container.getExecutor().getOriginal().getAttribute(attribute)).removeModifier(attributeModifier);
                    }
                }
        );

	}

	public Set<Map.Entry<Attribute, AttributeModifier>> getModfierEntry() {
		return this.BattleStyleStatModifier.entrySet();
	}

    @Override
    public void updateContainer(SkillContainer container) {
        super.updateContainer(container);
        if (BattleArtsAPI.debugMode && container.getDataManager().getDataValue(CoreAPIDataKeys.METER_FILL.get()) < (maxMeter * 100) && !container.getExecutor().isLogicalClient()) {
            container.getDataManager().setDataSyncF(CoreAPIDataKeys.METER_FILL.get(), data -> data + 1);
        }
        if (container.getExecutor() instanceof LocalPlayerPatch localPlayerPatch && localPlayerPatch.isTargetLockedOn())
        {
            container.getDataManager().setDataSync(CoreAPIDataKeys.COMBAT_COOLDOWN.get(), 50);
        }
        else
        {
            container.getDataManager().setDataSyncF(CoreAPIDataKeys.COMBAT_COOLDOWN.get(), data -> data - 1);
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
        float meterLevel = container.getDataManager().getDataValue(CoreAPIDataKeys.METER_FILL.get());
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

    public float[] getInnateSkillColor() {
        return innateSkillColor;
    }

	public static class Builder<T extends BattleStyle> extends SkillBuilder<BattleStyle>
	{
		protected BattleStyleCategory battleStyleCategory;


		public Builder()
		{
			super();
			battleStyleCategory = BattleStyleCategories.STARTING;
		}

		@Override
		public Builder<T> setRegistryName(ResourceLocation registryName) {
			this.registryName = registryName;
			return this;
		}

		public Builder<T> setCategory(SkillCategory category) {
			this.category = category;
			return this;
		}

		public Builder<T> setActivateType(ActivateType activateType) {
			this.activateType = activateType;
			return this;
		}

		public Builder<T> setResource(Resource resource) {
			this.resource = resource;
			return this;
		}

		public Builder<T> setCreativeTab(CreativeModeTab tab) {
			this.tab = tab;
			return this;
		}

		public Builder<T> setBattleStyleCategory(BattleStyleCategory category)
		{
			this.battleStyleCategory = category;
			return this;
		}
	}
}
