package net.forixaim.bs_api.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ProficiencyCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag>
{
	public static Capability<ProficiencyCapability> PROFICIENCY_CAPABILITY = CapabilityManager.get(new CapabilityToken<>()
	{
	});

	private ProficiencyCapability proficiencyCapability = null;

	private final LazyOptional<ProficiencyCapability> proficiencyCapabilityLazyOptional = LazyOptional.of(this::createProficiencyCapability);

	private ProficiencyCapability createProficiencyCapability()
	{
		if (this.proficiencyCapability == null)
		{
			this.proficiencyCapability = new ProficiencyCapability();
		}
		return this.proficiencyCapability;
	}

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction)
	{
		if (capability == PROFICIENCY_CAPABILITY)
		{
			return proficiencyCapabilityLazyOptional.cast();
		}
		return LazyOptional.empty();
	}

	@Override
	public CompoundTag serializeNBT() {
		return createProficiencyCapability().toNBTData();
	}

	@Override
	public void deserializeNBT(CompoundTag tag) {
		createProficiencyCapability().fromNBTData(tag);
	}
}
