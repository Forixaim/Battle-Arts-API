package net.forixaim.bs_api.battle_arts_skills.passive;

import net.forixaim.bs_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.bs_api.battle_arts_skills.battle_style.BattleStyle;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.registries.ForgeRegistries;
import yesman.epicfight.api.utils.ParseUtil;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillBuilder;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.passive.PassiveSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import java.util.List;
import java.util.Map;

public class BattleStyleDependentPassive extends PassiveSkill
{
	protected Map<Attribute, AttributeModifier> attributes;
	protected List<BattleStyle> battleStyles;

	public BattleStyleDependentPassive(SkillBuilder<? extends PassiveSkill> builder)
	{
		super(builder);
	}

	public boolean isApplicable(BattleStyle style)
	{
		return this.battleStyles.contains(style);
	}


	private boolean totalCheck(PlayerPatch<?> playerPatch)
	{
		BattleStyle style = (BattleStyle) playerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getSkill();
		return this.isApplicable(style);
	}

	@Override
	public void onInitiate(SkillContainer container)
	{
		container.setMaxDuration(this.maxDuration);
		if (!totalCheck(container.getExecutor()))
		{
			for (Map.Entry<Attribute, AttributeModifier> attributeAttributeModifierEntry : this.attributes.entrySet())
			{
				AttributeInstance attr = container.getExecutor().getOriginal().getAttribute(attributeAttributeModifierEntry.getKey());
				assert attr != null;
				if (!attr.hasModifier(attributeAttributeModifierEntry.getValue()))
				{
					attr.addTransientModifier(attributeAttributeModifierEntry.getValue());
				}
			}
		}
	}

	@Override
	public void setParams(CompoundTag parameters)
	{
		this.consumption = parameters.getFloat("consumption");
		this.maxDuration = parameters.getInt("max_duration");
		this.maxStackSize = parameters.contains("max_stacks") ? parameters.getInt("max_stacks") : 1;
		this.requiredXp = parameters.getInt("xp_requirement");
		this.attributes.clear();
		if (parameters.contains("attribute_modifiers")) {
			ListTag attributeList = parameters.getList("attribute_modifiers", 10);

			for (Tag tag : attributeList)
			{
				CompoundTag comp = (CompoundTag) tag;
				String attribute = comp.getString("attribute");
				Attribute attr = ForgeRegistries.ATTRIBUTES.getValue(ResourceLocation.parse(attribute));
				AttributeModifier modifier = ParseUtil.toAttributeModifier(comp);
				this.attributes.put(attr, modifier);
			}
		}
	}
}
