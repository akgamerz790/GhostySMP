package dev.akgamerz_790.ghostysmp.abilities;

import org.bukkit.Particle;
import org.bukkit.entity.Player;

import dev.akgamerz_790.ghostysmp.GhostySMP;

public class AnchorsAbility extends Ability {

    @Override
    public void activate(Player owner, Player target) {
        owner.getWorld().spawnParticle(Particle.CLOUD, owner.getLocation(),
            GhostySMP.getAnchorsParticleCount(),
            GhostySMP.getAnchorsParticleSpread(),
            GhostySMP.getAnchorsParticleSpread(),
            GhostySMP.getAnchorsParticleSpread(),
            0.1);
    }
}