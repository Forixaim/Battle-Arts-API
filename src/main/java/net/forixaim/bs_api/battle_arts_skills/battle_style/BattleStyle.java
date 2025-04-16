package net.forixaim.bs_api.battle_arts_skills.battle_style;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.forixaim.bs_api.battle_arts_skills.BattleArtsSkillCategories;
import net.forixaim.bs_api.battle_arts_skills.passive.BattleStyleDependentPassive;
import net.forixaim.bs_api.proficiencies.Proficiency;
import net.forixaim.bs_api.proficiencies.ProficiencyRank;
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
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.network.server.SPChangeSkill;
import yesman.epicfight.skill.*;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.WeaponCapability;
import yesman.epicfight.world.capabilities.item.WeaponCategory;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * This class extends the skill class and is mainly there as a framework for the Battle Styles which can modify certain things.
 */
public abstract class BattleStyle extends Skill
{
	private static final UUID UNIVERSAL_BATTLE_STYLE_UUID = UUID.fromString("705bfb84-a7c1-4726-bc7b-36cbabb84843");

	public static Builder<BattleStyle> CreateBattleStyle()
	{
		return (new Builder<>().setCategory(BattleArtsSkillCategories.BATTLE_STYLE).setResource(Resource.NONE));
	}

	public SkillDataKey<Boolean> getSneakIsDisabledKey()
	{
		return null;
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

	//From 0.0 to 1.0
	protected float criticalHitChance = 0.5F;
	protected float criticalHitDamage = 0.5F;

	protected Map<Proficiency, ProficiencyRank> requiredProficiencies;
	protected List<ResourceKey<DamageType>> immuneDamages;
	protected List<TagKey<DamageType>> immuneModdedDamages;
	protected List<Proficiency> proficiencySpecialization;
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
	protected boolean modifiesAttacks;

	/**
	 * super must be called when creating a new battle style.
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
		this.requiredProficiencies = Maps.newHashMap();
		this.proficiencySpecialization = Lists.newArrayList();
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

	public boolean checkProficiency(Proficiency proficiency)
	{
		for (Proficiency testProficiency : this.proficiencySpecialization)
		{
			if (testProficiency == proficiency)
			{
				return true;
			}
		}
		return false;
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

	public boolean canModifyAttacks()
	{
		return modifiesAttacks;
	}

	public Map<WeaponCategory, AnimationManager.AnimationAccessor<? extends StaticAnimation>> getWeaponDrawAnimations()
	{
		return weaponDrawAnimations;
	}

	public Map<LivingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>> getLivingMotionModifiers(LivingEntityPatch<?> entityPatch)
	{
		if (this.unarmedLivingMotions == null)
		{
			return Maps.newHashMap();
		}

		return this.unarmedLivingMotions;
	}

	@Override
	public void setParams(CompoundTag parameters) {
		super.setParams(parameters);

		this.BattleStyleStatModifier.clear();
		if (parameters.contains("attribute_modifiers")) {
			ListTag attributeList = parameters.getList("attribute_modifiers", 10);

			for (Tag tag : attributeList) {
				CompoundTag comp = (CompoundTag)tag;
				String attribute = comp.getString("attribute");
				Attribute attr = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(attribute));
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
		for (Map.Entry<Attribute, AttributeModifier> stat : this.BattleStyleStatModifier.entrySet()) {
			AttributeInstance attr = container.getExecutor().getOriginal().getAttribute(stat.getKey());

			assert attr != null;
			if (!attr.hasModifier(stat.getValue())) {
				attr.addTransientModifier(stat.getValue());
			}
		}
		container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.DEALT_DAMAGE_EVENT_DAMAGE, UNIVERSAL_BATTLE_STYLE_UUID, event ->
		{
			//Generate a number between 0 inclusive and 1 inclusive
			float random = container.getExecutor().getOriginal().getRandom().nextFloat();
			CriticalHitEvent crit = ForgeHooks.getCriticalHit(event.getPlayerPatch().getOriginal(), event.getTarget(), false, random <= criticalHitChance ? 1 + getCriticalHitDamage() : 1.0f);
			if (crit != null)
			{
				event.setAttackDamage(event.getAttackDamage() * crit.getDamageModifier());
				event.getPlayerPatch().playSound(SoundEvents.PLAYER_ATTACK_CRIT, 1.0F, 1.0F);
			}

		});
	}

	private void removeBattleStyleDependentSkills(ServerPlayerPatch playerPatch)
	{
		for (SkillContainer skillContainer : playerPatch.getSkillCapability().skillContainers)
		{
			if (skillContainer.getSkill() instanceof BattleStyleDependentPassive bsd)
			{
				if (bsd.isApplicable(this))
				{
					skillContainer.setSkill(null);
					EpicFightNetworkManager.sendToPlayer(new SPChangeSkill(skillContainer.getSlot(), "", SPChangeSkill.State.DISABLE), playerPatch.getOriginal());
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

		for (Map.Entry<Attribute, AttributeModifier> stat : this.BattleStyleStatModifier.entrySet()) {
			AttributeInstance attr = container.getExecutor().getOriginal().getAttribute(stat.getKey());

			assert attr != null;
			if (attr.hasModifier(stat.getValue())) {
				attr.removeModifier(stat.getValue());
			}
		}
	}

	public Set<Map.Entry<Attribute, AttributeModifier>> getModfierEntry() {
		return this.BattleStyleStatModifier.entrySet();
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void drawOnGui(BattleModeGui gui, SkillContainer container, GuiGraphics guiGraphics, float x, float y) {
		PoseStack poseStack = guiGraphics.pose();
		poseStack.pushPose();
		poseStack.translate(0, (float)gui.getSlidingProgression(), 0);
		guiGraphics.blit(this.getSkillTexture(), (int)x, (int)y, 24, 24, 0, 0, 1, 1, 1, 1);
		String remainTime = String.format("%.0f", container.getMaxResource() - container.getResource());
		guiGraphics.drawString(gui.getFont(), remainTime, x + 12 - 4 * remainTime.length(), (y+6), 16777215, true);
		poseStack.popPose();
	}

    public float[] getInnateSkillColor() {
        return innateSkillColor;
    }

	public static class Builder<T extends BattleStyle> extends SkillBuilder<BattleStyle>
	{
		protected List<Proficiency> proficiencies;
		protected BattleStyleCategory battleStyleCategory;


		public Builder()
		{
			super();
			proficiencies = Lists.newArrayList();
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

		public Builder<T> addProficiencySpecialization(Proficiency proficiency)
		{
			proficiencies.add(proficiency);
			return this;
		}

		public Builder<T> setBattleStyleCategory(BattleStyleCategory category)
		{
			this.battleStyleCategory = category;
			return this;
		}
	}
}
