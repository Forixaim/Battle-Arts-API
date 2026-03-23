package net.forixaim.battle_arts_api.battle_arts_skills.active;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillCategories;
import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.battle_arts_api.battle_arts_skills.CoreAPIDataKeys;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillBuilder;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.WeaponCategory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * This class is not supposed to be used, all class builders will exist within their own sub abstract classes.
 */
public abstract class ActiveSkill extends Skill
{
	//General Lists of allowed weapons and properties.
	protected List<WeaponCategory> allowedWeapons = Lists.newArrayList();
	protected List<Map<AnimationProperty.AttackPhaseProperty<?>, Object>> properties;
	protected float manaConsumption;
    protected float meterUsage;
	protected float staminaConsumption;

	public ActiveSkill(SkillBuilder<?> builder) {
		super(builder);
		this.properties = Lists.newArrayList();
	}

    public static <B extends SkillBuilder<B>> SkillBuilder<?> createActiveSkill(Function<B, ? extends Skill> constructor)
    {
        return new SkillBuilder<>(constructor).setResource(Resource.COOLDOWN);
    }

	@Override
	public void loadDatapackParameters(CompoundTag parameters)
	{
		super.loadDatapackParameters(parameters);
		this.manaConsumption = parameters.getFloat("mana_consumption");
        this.meterUsage = parameters.getFloat("meter_usage");
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

    protected boolean hasMeter(SkillContainer container)
    {

        if (container.getExecutor().getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getDataManager().hasData(CoreAPIDataKeys.METER_FILL))
        {
            return container.getExecutor().getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getDataManager().getDataValue(CoreAPIDataKeys.METER_FILL) >= meterUsage;
        }
        return meterUsage <= 0;
    }

    @Override
	public boolean canExecute(SkillContainer container) {
		ItemStack weapon = container.getExecutor().getOriginal().getMainHandItem();
		WeaponCategory weaponCategory = EpicFightCapabilities.getItemStackCapability(weapon).getWeaponCategory();
		if (container.getExecutor().isLogicalClient())
		{
			return super.canExecute(container);

		} else {
            return super.canExecute(container) && weaponCategoryMatch(weaponCategory) && hasMeter(container)
					&& container.getExecutor().getOriginal().getVehicle() == null && (!container.getExecutor().getSkill(this).isActivated() || this.activateType == ActivateType.TOGGLE);
		}
	}

    private float consumeMeter(float data)
    {
        return data - meterUsage;
    }

	@Override
	public void executeOnServer(SkillContainer container, CompoundTag args)
	{
		if (container.getExecutor() instanceof ServerPlayerPatch executor)
		{
			executor.setStamina(executor.getStamina() - staminaConsumption);
		}
        if (hasMeter(container))
            container.getExecutor().getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getDataManager().setDataSyncF(CoreAPIDataKeys.METER_FILL, this::consumeMeter);
		super.executeOnServer(container, args);
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
