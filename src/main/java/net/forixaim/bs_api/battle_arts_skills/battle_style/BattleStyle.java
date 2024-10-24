package net.forixaim.bs_api.battle_arts_skills.battle_style;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.forixaim.bs_api.battle_arts_skills.BattleArtsSkillCategories;
import net.forixaim.bs_api.proficiencies.Proficiency;
import net.forixaim.bs_api.proficiencies.ProficiencyRank;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import yesman.epicfight.api.animation.AnimationProvider;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.utils.ParseUtil;
import yesman.epicfight.client.gui.BattleModeGui;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategory;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.item.Style;
import yesman.epicfight.world.capabilities.item.WeaponCategory;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class extends the skill class and is mainly there as a framework for the Battle Styles which can modify certain things.
 */
public abstract class BattleStyle extends Skill
{
	public static Builder<BattleStyle> CreateBattleStyle()
	{
		return (new Builder<>().setCategory(BattleArtsSkillCategories.BATTLE_STYLE).setResource(Resource.NONE));
	}

	protected int proficiencyXpPerKill = 0;

	protected List<Pair<Proficiency, ProficiencyRank>> requiredProficiencies;
	protected List<ResourceKey<DamageType>> immuneDamages;
	protected List<TagKey<DamageType>> immuneModdedDamages;
	protected List<Proficiency> proficiencySpecialization;
	protected BattleStyleCategory category;

	protected List<AnimationProvider<?>> unarmedAttackAnimations;
	protected Map<LivingMotion, AnimationProvider<?>> unarmedLivingMotions;
	protected Map<LivingMotion, AnimationProvider<?>> unarmedBattleMotions;


	private final Map<Attribute, AttributeModifier> BattleStyleStatModifier;
	protected Map<LivingMotion, StaticAnimation> livingMotionModifiers;
	protected List<Pair<WeaponCategory, AnimationProvider<StaticAnimation>>> weaponDrawAnimations;
	protected boolean modifiesAttacks;

	public BattleStyle(Builder<?> builder)
	{
		super (builder);
		this.unarmedAttackAnimations = Lists.newArrayList();
		this.unarmedLivingMotions = Maps.newHashMap();
		this.BattleStyleStatModifier = Maps.newHashMap();
		this.livingMotionModifiers = Maps.newHashMap();
		this.immuneDamages = Lists.newArrayList();
		this.immuneModdedDamages = Lists.newArrayList();
		this.requiredProficiencies = Lists.newArrayList();
		this.proficiencySpecialization = Lists.newArrayList();
		this.weaponDrawAnimations = Lists.newArrayList();
		this.category = builder.battleStyleCategory;
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

	public Map<LivingMotion, AnimationProvider<?>> getUnarmedBattleMotions()
	{
		return unarmedBattleMotions;
	}

	public List<AnimationProvider<?>> getUnarmedAttackAnimations()
	{
		return unarmedAttackAnimations;
	}

	public Map<LivingMotion, AnimationProvider<?>> getUnarmedLivingMotions()
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

	public List<Pair<WeaponCategory, AnimationProvider<StaticAnimation>>> getWeaponDrawAnimations()
	{
		return weaponDrawAnimations;
	}

	public Map<LivingMotion, StaticAnimation> getLivingMotionModifiers(LivingEntityPatch<?> entityPatch)
	{
		if (this.livingMotionModifiers == null)
		{
			return Maps.newHashMap();
		}

		return this.livingMotionModifiers;
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
	}

	@Override
	public void onInitiate(SkillContainer container) {
		for (Map.Entry<Attribute, AttributeModifier> stat : this.BattleStyleStatModifier.entrySet()) {
			AttributeInstance attr = container.getExecuter().getOriginal().getAttribute(stat.getKey());

			assert attr != null;
			if (!attr.hasModifier(stat.getValue())) {
				attr.addTransientModifier(stat.getValue());
			}
		}
	}

	@Override
	public void onRemoved(SkillContainer container) {
		for (Map.Entry<Attribute, AttributeModifier> stat : this.BattleStyleStatModifier.entrySet()) {
			AttributeInstance attr = container.getExecuter().getOriginal().getAttribute(stat.getKey());

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
		guiGraphics.drawString(gui.font, remainTime, x + 12 - 4 * remainTime.length(), (y+6), 16777215, true);
		poseStack.popPose();
	}

	public static class Builder<T extends BattleStyle> extends Skill.Builder<BattleStyle>
	{
		protected List<Proficiency> proficiencies;
		protected BattleStyleCategory battleStyleCategory;


		public Builder()
		{
			super();
			proficiencies = Lists.newArrayList();
			battleStyleCategory = BattleStyleCategories.STARTING;
		}

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
