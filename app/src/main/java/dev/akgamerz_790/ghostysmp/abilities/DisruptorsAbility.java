package dev.akgamerz_790.ghostysmp.abilities;

import org.bukkit.Particle;
import org.bukkit.entity.Player;

import dev.akgamerz_790.ghostysmp.GhostySMP;

public class DisruptorsAbility extends Ability {

    @Override
    public void activate(Player owner, Player target) {
        if (target != null && !target.equals(owner)) {
            GhostManager.reflectUntil.put(owner.getUniqueId(), System.currentTimeMillis() + GhostySMP.getDisruptorsReflectDurationTicks());
            owner.getWorld().spawnParticle(Particle.FLAME, owner.getLocation(),
                GhostySMP.getDisruptorsParticleCount(),
                GhostySMP.getDisruptorsParticleSpread(),
                GhostySMP.getDisruptorsParticleSpread(),
                GhostySMP.getDisruptorsParticleSpread(), 0.1);
        }
    }
}
