package net.forixaim.battle_arts_api.registry;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import net.forixaim.battle_arts_api.BattleArtsAPI;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.forgeevent.SkillLootTableRegistryEvent;
import yesman.epicfight.data.loot.function.SetSkillFunction;
import yesman.epicfight.world.item.EpicFightItems;

@Mod.EventBusSubscriber(modid = BattleArtsAPI.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LootTablesModification
{
    @SubscribeEvent
    public static void onModifyLootTable(SkillLootTableRegistryEvent event)
    {
        if (ModList.get().isLoaded(IronsSpellbooks.MODID))
        {
            event.add(EntityType.ZOMBIE, LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .when(LootItemRandomChanceCondition.randomChance(0.1f))
                    .add(LootItem.lootTableItem(EpicFightItems.SKILLBOOK.get()).apply(
                            SetSkillFunction.builder(1.0f, "battlearts_api:mana_charge")
                    )));
        }

    }
}
