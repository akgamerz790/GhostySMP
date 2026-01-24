package dev.akgamerz_790.ghostysmp.abilities;

import org.bukkit.Particle;
import org.bukkit.entity.Player;

import dev.akgamerz_790.ghostysmp.GhostySMP;

public class InfectorAbility extends Ability {

    @Override
    public void activate(Player owner, Player target) {
        if (target != null && !target.equals(owner)) {
            target.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, owner.getLocation(),
                GhostySMP.getInfectorParticleCount(),
                GhostySMP.getInfectorParticleSpread(),
                GhostySMP.getInfectorParticleSpread(),
                GhostySMP.getInfectorParticleSpread(), 0.1);
        }
    }
}
