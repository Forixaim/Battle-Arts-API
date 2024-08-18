package net.forixaim.bs_api.battle_arts_skills.battle_style;

import yesman.epicfight.api.utils.ExtendableEnum;
import yesman.epicfight.api.utils.ExtendableEnumManager;
import yesman.epicfight.world.capabilities.item.WeaponCategory;

public interface BattleStyleCategory extends ExtendableEnum
{
	ExtendableEnumManager<BattleStyleCategory> ENUM_MANAGER = new ExtendableEnumManager<> ("battle_style_category");
}
