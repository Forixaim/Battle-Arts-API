package net.forixaim.battle_arts_api.client.input.handlers;

import net.forixaim.battle_arts_api.client.input.controller.BattleArtsControllerModProvider;
import net.forixaim.battle_arts_api.client.input.controller.IBattleArtsControllerMod;

public class SharedMethods
{
    static IBattleArtsControllerMod getAPI()
    {
        return BattleArtsControllerModProvider.get();
    }
}
