package net.forixaim.battle_arts_api.battle_arts_skills.battle_style;

import yesman.epicfight.api.utils.ExtensibleEnum;
import yesman.epicfight.api.utils.ExtensibleEnumManager;

public interface BattleStyleCategory extends ExtensibleEnum
{
	ExtensibleEnumManager<BattleStyleCategory> ENUM_MANAGER = new ExtensibleEnumManager<> ("battle_style_category");
}
