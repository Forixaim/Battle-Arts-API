package net.forixaim.bs_api.registry;

import net.forixaim.bs_api.BattleArtsAPI;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import yesman.epicfight.client.particle.HitParticle;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.particle.HitParticleType;

public class ParticleRegistry
{
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(Registries.PARTICLE_TYPE, BattleArtsAPI.MOD_ID);

    public static final RegistryObject<HitParticleType> CHARGE_AURA = PARTICLES.register("charge_aura", () -> new HitParticleType(true, HitParticleType.CENTER_OF_TARGET, HitParticleType.ATTACKER_XY_ROTATION));
}
