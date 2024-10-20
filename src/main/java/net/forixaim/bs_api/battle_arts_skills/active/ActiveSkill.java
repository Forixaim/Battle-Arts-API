package net.forixaim.bs_api.battle_arts_skills.active;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.WeaponCategory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This class is not supposed to be used, all class builders will exist within their own sub abstract classes.
 */
public abstract class ActiveSkill extends Skill
{
	//General Lists of allowed weapons and properties.
	protected List<WeaponCategory> allowedWeapons = Lists.newArrayList();
	protected List<Map<AnimationProperty.AttackPhaseProperty<?>, Object>> properties;

	//Resource Consumption
	protected float manaConsumption;
	protected float staminaConsumption;

	public ActiveSkill(Builder<? extends Skill> builder) {
		super(builder);

		this.properties = Lists.newArrayList();
	}

	@Override
	public void setParams(CompoundTag parameters)
	{
		super.setParams(parameters);
		this.manaConsumption = parameters.getFloat("mana_consumption");
		this.staminaConsumption = parameters.getFloat("stamina_consumption");
	}


	protected boolean weaponCategoryMatch(WeaponCategory category)
	{
		for (WeaponCategory category1 : allowedWeapons)
		{
			if (category1 == category)
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean canExecute(PlayerPatch<?> executer) {
		ItemStack weapon = executer.getOriginal().getMainHandItem();
		WeaponCategory weaponCategory = EpicFightCapabilities.getItemStackCapability(weapon).getWeaponCategory();
		if (executer.isLogicalClient())
		{
			return super.canExecute(executer);

		} else {
			ItemStack itemstack = executer.getOriginal().getMainHandItem();

			return super.canExecute(executer) && weaponCategoryMatch(weaponCategory)
					&& executer.getOriginal().getVehicle() == null && (!executer.getSkill(this).isActivated() || this.activateType == ActivateType.TOGGLE);
		}
	}

	@Override
	public void executeOnServer(ServerPlayerPatch executor, FriendlyByteBuf args)
	{
		executor.setStamina(executor.getStamina() - staminaConsumption);

		super.executeOnServer(executor, args);
	}

	@Override
	public void onInitiate(SkillContainer container) {
		super.onInitiate(container);
	}

	@SuppressWarnings("unchecked")
	protected <V> Optional<V> getProperty(AnimationProperty.AttackPhaseProperty<V> propertyKey, Map<AnimationProperty.AttackPhaseProperty<?>, Object> map) {
		return (Optional<V>) Optional.ofNullable(map.get(propertyKey));
	}

	public ActiveSkill newProperty() {
		this.properties.add(Maps.newHashMap());

		return this;
	}

	public <T> ActiveSkill addProperty(AnimationProperty.AttackPhaseProperty<T> propertyKey, T object) {
		this.properties.get(properties.size() - 1).put(propertyKey, object);

		return this;
	}
}
