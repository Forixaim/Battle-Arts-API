package net.forixaim.bs_api.events;

import com.mna.api.events.SpellCastEvent;
import com.mna.api.spells.collections.Shapes;
import com.mna.api.spells.parts.Shape;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

import java.util.Objects;

public class OptionalEvents
{
	@SubscribeEvent
	public static void onCast(SpellCastEvent event)
	{
		if (event.getSpell().getShape() != null)
		{
			Shape spellShape = Objects.requireNonNull(event.getSpell().getShape()).getPart();
			LivingEntityPatch<?> entityPatch = EpicFightCapabilities.getEntityPatch(event.getSource().getCaster(), LivingEntityPatch.class);

		}
	}
}
